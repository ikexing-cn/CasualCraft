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
import java.util.*

@ZenRegister
@ModOnly("botania")
@ZenClass("mods.casualcraft.botania.ManaInfusion")
object ManaInfusion {

    @JvmStatic
    @ZenMethod
    fun addRecipe(output: IItemStack, input: IIngredient, mana: Int, @Optional catalystState: Any?) {
        Main.LATE_ADD_ACTIONS.add(AddRecipe(output, input, mana, catalystState))
    }

    @JvmStatic
    @ZenMethod
    fun addAlchemy(output: IItemStack, input: IIngredient, mana: Int) {
        this.addRecipe(output, input, mana, CraftTweakerMC.getBlockState(RecipeManaInfusion.alchemyState))
    }

    @JvmStatic
    @ZenMethod
    fun addConjuration(output: IItemStack, input: IIngredient, mana: Int) {
        this.addRecipe(output, input, mana, CraftTweakerMC.getBlockState(RecipeManaInfusion.conjurationState))
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
            when (val catalyst = catalystState) {
                is IBlockState -> recipe.catalyst = catalyst.original()
                is IItemStack -> recipe.catalyst = catalyst.toBlockState()
                null -> {}
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

        override fun apply() {
            val toRemove = BotaniaAPI.manaInfusionRecipes
                .filter { it.output != null && it.output.matches(output.original(), true) }
            if (toRemove.isEmpty()) {
                logError("No Mana Infusion Recipe for $output")
            } else {
                toRemove.forEach {
                    BotaniaAPI.manaInfusionRecipes.removeIf { r -> r.output.matches(it.output, true) }
                }
            }
        }

        override fun describe(): String {
            val toRemove = BotaniaAPI.manaInfusionRecipes
                .filter { it.output != null && it.output.matches(output.original(), true) }.toList()
            return "Remove ${toRemove.size} Mana Infusion Recipe(s) for $output"
        }

    }

}


