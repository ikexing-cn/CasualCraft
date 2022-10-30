package me.ikexing.casualcraft.utils

import crafttweaker.api.block.IBlockState
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import crafttweaker.api.item.IngredientStack
import crafttweaker.api.minecraft.CraftTweakerMC
import crafttweaker.api.oredict.IOreDictEntry
import net.minecraft.block.Block
import net.minecraft.item.ItemStack

fun IItemStack.original(): ItemStack {
    return CraftTweakerMC.getItemStack(this)
}

@Suppress("DEPRECATION")
fun IItemStack.toBlockState(): net.minecraft.block.state.IBlockState {
    val original = this.original()
    return Block.getBlockFromItem(original.item).getStateFromMeta(original.metadata)
}

fun IBlockState.original(): net.minecraft.block.state.IBlockState {
    return CraftTweakerMC.getBlockState(this)
}

fun IIngredient.toObject(): Any? {
    return when (this) {
        is IOreDictEntry -> this.name
        is IItemStack -> this.original()
        is IngredientStack -> this.items
        else -> null
    }
}
