package me.ikexing.casualcraft.utils

import crafttweaker.CraftTweakerAPI
import crafttweaker.api.block.IBlockState
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import crafttweaker.api.item.IngredientStack
import crafttweaker.api.minecraft.CraftTweakerMC
import crafttweaker.api.oredict.IOreDictEntry
import crafttweaker.api.potions.IPotionEffect
import me.ikexing.casualcraft.Main
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect

fun IItemStack.original(): ItemStack {
    return CraftTweakerMC.getItemStack(this)
}

@Suppress("DEPRECATION")
fun IItemStack.toBlockState(): net.minecraft.block.state.IBlockState {
    val original = this.original()
    return Block.getBlockFromItem(original.item).getStateFromMeta(original.metadata)
}

fun net.minecraft.block.state.IBlockState.toCrtType(): IBlockState {
    return CraftTweakerMC.getBlockState(this)
}


fun IBlockState.original(): net.minecraft.block.state.IBlockState {
    return CraftTweakerMC.getBlockState(this)
}

fun IIngredient.toObject(): Any {
    return when (this) {
        is IOreDictEntry -> this.name
        is IItemStack -> this.original()
        is IngredientStack -> this.items
        else -> ItemStack.EMPTY.toCrtType()
    }
}

fun IIngredient.toItems(): List<ItemStack> {
    return when (this) {
        is IItemStack -> listOf(this.original())
        else -> this.items.map { it.original().splitStack(this.amount) }
    }
}

fun ItemStack.toCrtType(): IItemStack {
    return CraftTweakerMC.getIItemStack(this)
}

fun IPotionEffect.original(): PotionEffect {
    return CraftTweakerMC.getPotionEffect(this)
}

fun logError(message: String) {
    CraftTweakerAPI.logError(
        "[${Main.MOD_NAME}] Failed to process the action correctly.",
        IllegalArgumentException(message)
    )
}