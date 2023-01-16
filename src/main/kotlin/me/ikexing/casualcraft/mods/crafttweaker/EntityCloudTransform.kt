package me.ikexing.casualcraft.mods.crafttweaker

import crafttweaker.CraftTweakerAPI
import crafttweaker.IAction
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import crafttweaker.api.potions.IPotionEffect
import me.ikexing.casualcraft.recipes.RecipeBaseTransform
import me.ikexing.casualcraft.recipes.RecipeEntityCloudRecipe
import me.ikexing.casualcraft.utils.original
import me.ikexing.casualcraft.utils.toCrtType
import me.ikexing.casualcraft.utils.toItems
import stanhebben.zenscript.annotations.Optional
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

@ZenRegister
@ZenClass("mods.casualcraft.EntityCloudTransform")
object EntityCloudTransform {

    @JvmStatic
    @ZenMethod
    fun addRecipe(
        output: IItemStack, input: Array<IIngredient>, potions: Array<IPotionEffect>,
        @Optional(valueDouble = 1.0) chance: Double, @Optional(valueLong = 1) priority: Int) {
        CraftTweakerAPI.apply(AddRecipe(output, input, potions, chance, priority))
    }

    @JvmStatic
    @ZenMethod
    fun addRecipe(
        output: IItemStack, input: Array<IIngredient>, potion: IPotionEffect,
        @Optional(valueDouble = 1.0) chance: Double, @Optional(valueLong = 1) priority: Int) {
        this.addRecipe(output, input, arrayOf(potion), chance, priority)
    }

    class AddRecipe(
        private val output: IItemStack, private val input: Array<IIngredient>, private val potions: Array<IPotionEffect>,
        @Optional(valueDouble = 1.0) private val chance: Double, @Optional(valueLong = 1) private val priority: Int
    ) : IAction {

        override fun apply() {
            RecipeBaseTransform.addRecipe(
                RecipeEntityCloudRecipe(potions.map { it.original() }, output.original(), input.map { it.toItems() }, chance, priority)
            )
        }

        override fun describe(): String {
            return "Add EntityCloudTransform recipe for ${
                input.flatMap { it -> it.toItems().map { it.toCrtType().toCommandString() } }
            } -> $output with ${
                potions.map { "{potion: ${it.effectName}, duration: ${it.duration}, amplifier: ${it.amplifier}}" }
            }, $chance chance and $priority priority"
        }

    }

}