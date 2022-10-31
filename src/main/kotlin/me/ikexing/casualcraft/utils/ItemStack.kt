package me.ikexing.casualcraft.utils

import crafttweaker.api.minecraft.CraftTweakerMC
import crafttweaker.mc1120.data.NBTConverter
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.oredict.OreDictionary

fun ItemStack.matches(other: ItemStack): Boolean {
    return if (Loader.isModLoaded("crafttweaker"))
        CraftTweakerMC.getIItemStack(this).matches(CraftTweakerMC.getIItemStack(other))
    else if (other.hasTagCompound()) this.matchesTag(other) else simpleEquals(other)
}

fun ItemStack.matchesTag(other: ItemStack): Boolean {
    val thisTag = this.tagCompound
    val stackTag: NBTTagCompound? = other.tagCompound
    if (this.hasTagCompound() != other.hasTagCompound())
        return false

    val itemMatches = other.item === this.item && this.metaEquals(other)

    if (itemMatches) {
        if (thisTag == null && stackTag == null)
            return true

        if (!NBTConverter.from(thisTag, true).contains(NBTConverter.from(stackTag, true)))
            return false
    }
    return itemMatches
}

fun ItemStack.metaEquals(other: ItemStack): Boolean {
    return other.itemDamage == OreDictionary.WILDCARD_VALUE
            || this.itemDamage == OreDictionary.WILDCARD_VALUE
            || other.itemDamage == this.itemDamage || !other.hasSubtypes
}

fun ItemStack.simpleEquals(other: ItemStack) = (!this.isEmpty && !other.isEmpty &&
        this.item === other.item &&
        this.count >= other.count &&
        this.metaEquals(other)
        && !other.item.isDamageable)