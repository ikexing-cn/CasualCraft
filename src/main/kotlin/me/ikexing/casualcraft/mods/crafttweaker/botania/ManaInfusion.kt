package me.ikexing.casualcraft.mods.crafttweaker.botania

import crafttweaker.IAction
import crafttweaker.annotations.ModOnly
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.block.IBlockState
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import crafttweaker.api.minecraft.CraftTweakerMC
import me.ikexing.casualcraft.Main
import me.ikexing.casualcraft.utils.*
import stanhebben.zenscript.annotations.Optional
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.RecipeManaInfusion

@ZenRegister
@ModOnly("botania")
@ZenClass("mods.casualcraft.botania.ManaInfusion")
object ManaInfusion {

    @JvmStatic
    @ZenMethod
    fun addInfusion(output: IItemStack, input: IIngredient, mana: Int, @Optional catalystState: Any?) {
        Main.LATE_ADD_ACTIONS.add(AddRecipe(output, input, mana, catalystState))
    }

    @JvmStatic
    @ZenMethod
    fun addAlchemy(output: IItemStack, input: IIngredient, mana: Int) {
        this.addInfusion(output, input, mana, CraftTweakerMC.getBlockState(RecipeManaInfusion.alchemyState))
    }

    @JvmStatic
    @ZenMethod
    fun addConjuration(output: IItemStack, input: IIngredient, mana: Int) {
        this.addInfusion(output, input, mana, CraftTweakerMC.getBlockState(RecipeManaInfusion.conjurationState))
    }

    @JvmStatic
    @ZenMethod
    fun removeRecipe(output: IItemStack) {
        Main.LATE_REMOVE_ACTIONS.add(RemoveRecipe(output))
    }

    private class AddRecipe(
        private var output: IItemStack,
        private var input: IIngredient,
        private var mana: Int,
        private var catalystState: Any?
    ) : IAction {
        override fun apply() {
            val recipe = RecipeManaInfusion(output.original(), input.toObject(), mana)
            val catalyst = catalystState ?: return
            // FIXME: when catalyst is null, it add normal recipe
            when (catalyst) {
                is IBlockState -> recipe.catalyst = catalyst.original()
                is IItemStack -> recipe.catalyst = catalyst.toBlockState()
                else -> logError("Type of catalystState is not IBlockState or IItemStack")
            }
            BotaniaAPI.manaInfusionRecipes.add(recipe)
        }

        override fun describe(): String {
            return "Add Mana Infusion Recipe: $input -> $output with $mana mana" + if (catalystState != null) " and catalyst $catalystState" else ""
        }

    }

    private class RemoveRecipe(
        private var output: IItemStack
    ) : IAction {
        val toRemove = BotaniaAPI.manaInfusionRecipes
            .filter { it.output != null && output.matches(it.output.toCrtType()) }.toList()

        override fun apply() {
            if (toRemove.isEmpty()) {
                logError("No Mana Infusion Recipe for $output")
            } else if (BotaniaAPI.manaInfusionRecipes.removeIf { it in toRemove }) {
                logError("Failed to remove Mana Infusion Recipe for $output")
            }
        }

        override fun describe(): String {
            return ("Remove ${toRemove.size} Mana Infusion Recipe(s) for $output")
        }

    }

}


