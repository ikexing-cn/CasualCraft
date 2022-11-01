package me.ikexing.casualcraft.utils

import crafttweaker.mc1120.data.NBTConverter
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.oredict.OreDictionary

fun ItemStack.matches(other: ItemStack): Boolean {
    val thisTag = this.tagCompound
    val stackTag: NBTTagCompound? = other.tagCompound
    if (this.hasTagCompound() != other.hasTagCompound())
        return false

    val itemMatches = (this.item === other.item && this.count == other.count) &&
            (other.itemDamage == OreDictionary.WILDCARD_VALUE || this.itemDamage == OreDictionary.WILDCARD_VALUE || other.metadata == this.metadata)

    if (itemMatches) {
        if (thisTag == null && stackTag == null)
            return true

        if (!NBTConverter.from(thisTag, true).contains(NBTConverter.from(stackTag, true)))
            return false
    }
    return itemMatches
}

fun ItemStack.setCountAndReturnThis(count: Int): ItemStack {
    this.count = count
    return this
}