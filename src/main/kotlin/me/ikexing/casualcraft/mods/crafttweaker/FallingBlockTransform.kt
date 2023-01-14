package me.ikexing.casualcraft.mods.crafttweaker

import crafttweaker.CraftTweakerAPI
import crafttweaker.IAction
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.block.IBlockState
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import me.ikexing.casualcraft.recipes.RecipeBaseTransform
import me.ikexing.casualcraft.recipes.RecipeFallingBlockTransform
import me.ikexing.casualcraft.utils.original
import me.ikexing.casualcraft.utils.toBlockState
import me.ikexing.casualcraft.utils.toCrtType
import me.ikexing.casualcraft.utils.toItems
import stanhebben.zenscript.annotations.Optional
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenRegister
@ZenClass("mods.casualcraft.FallingBlockTransform")
object FallingBlockTransform {

    @JvmStatic
    @ZenMethod
    fun addRecipe(
        block: IBlockState, output: IItemStack, input: Array<IIngredient>,
        @Optional(valueBoolean = true) exactMatch: Boolean,
        @Optional(valueDouble = 1.0) chance: Double, @Optional(valueLong = 1) priority: Int
    ) {
        CraftTweakerAPI.apply(AddRecipe(block, output, exactMatch, input, chance, priority))
    }

    @JvmStatic
    @ZenMethod
    fun addRecipe(
        block: IItemStack, output: IItemStack, input: Array<IIngredient>,
        @Optional(valueBoolean = true) exactMatch: Boolean,
        @Optional(valueDouble = 1.0) chance: Double, @Optional(valueLong = 1) priority: Int
    ) {
        this.addRecipe(block.toBlockState().toCrtType(), output, input, exactMatch, chance, priority)
    }

    class AddRecipe(
        private val block: IBlockState, private val output: IItemStack, private val exactMatch: Boolean,
        private val input: Array<IIngredient>, private val chance: Double, private val priority: Int
    ) : IAction {

        override fun apply() {
            RecipeBaseTransform.addRecipe(
                RecipeFallingBlockTransform(block.original(), exactMatch, output.original(), input.map { it.toItems() }.toList(), chance, priority)
            )
        }

        override fun describe(): String {
            return "Add FallingBlockTransform recipe for ${
                input.flatMap { it -> it.toItems().map { it.toCrtType().toCommandString() }  }
            } -> $output with $block, $chance chance and $priority priority"
        }

    }

}