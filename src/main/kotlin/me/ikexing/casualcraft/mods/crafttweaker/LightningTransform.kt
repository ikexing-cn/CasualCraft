package me.ikexing.casualcraft.mods.crafttweaker

import crafttweaker.IAction
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import me.ikexing.casualcraft.Main
import me.ikexing.casualcraft.recipes.RecipeLightningTransform
import me.ikexing.casualcraft.utils.original
import me.ikexing.casualcraft.utils.toItems
import stanhebben.zenscript.annotations.Optional
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenRegister
@ZenClass("mods.casualcraft.LightningTransform")
object LightningTransform {

    @JvmStatic
    @ZenMethod
    fun addRecipe(
        output: IItemStack, input: MutableList<IIngredient>,
        @Optional(valueDouble = 1.0) chance: Double, @Optional(valueLong = 1) priority: Int
    ) {
        Main.LATE_ADD_ACTIONS.add(AddRecipe(output, input, chance, priority))
    }

    class AddRecipe(
        private val output: IItemStack, private val input: MutableList<IIngredient>,
        private val chance: Double, private val priority: Int
    ) : IAction {

        override fun apply() {
            val nonStackable = input.filter { it.amount > 1 }.filter { it.items.any { stack -> !stack.isStackable } }
            input.addAll(nonStackable)
            val mcInput = input.map { it.toItems() }
            RecipeLightningTransform.addRecipe(output.original(), mcInput, chance, priority)
        }

        override fun describe(): String {
            return "Add LightningTransform recipe for $input -> $output with $chance chance and $priority priority"
        }

    }

}