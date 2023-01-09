package me.ikexing.casualcraft.utils

import crafttweaker.mc1120.data.NBTConverter
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.oredict.OreDictionary

fun ItemStack.matches(other: ItemStack, matchCount: Boolean): Boolean {
    val thisCopy = this.copy().splitStack(1)
    val otherCopy = other.copy().splitStack(1)

    val thisTag = thisCopy.tagCompound
    val stackTag: NBTTagCompound? = otherCopy.tagCompound
    if (thisCopy.hasTagCompound() != otherCopy.hasTagCompound()) return false

    val itemMatches =
        (thisCopy.item === otherCopy.item) && (if (matchCount) this.count == other.count else thisCopy.count == otherCopy.count && this.count >= other.count) &&
                (otherCopy.itemDamage == OreDictionary.WILDCARD_VALUE || thisCopy.itemDamage == OreDictionary.WILDCARD_VALUE || otherCopy.metadata == thisCopy.metadata)

    if (itemMatches) {
        if (thisTag == null && stackTag == null) return true

        if (!NBTConverter.from(thisTag, true).contains(NBTConverter.from(stackTag, true))) return false
    }
    return itemMatches
}
