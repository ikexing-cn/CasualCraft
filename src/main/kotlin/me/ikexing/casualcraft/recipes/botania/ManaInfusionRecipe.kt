package me.ikexing.casualcraft.recipes.botania

import me.ikexing.casualcraft.utils.matches
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.RecipeManaInfusion

object ManaInfusionRecipe {

    fun addRecipe(output: ItemStack, input: Any, mana: Int, catalystState: IBlockState?) {
        val recipe = RecipeManaInfusion(output, input, mana)
        when (catalystState) {
            is IBlockState -> recipe.catalyst = catalystState
            else -> {}
        }
        BotaniaAPI.manaInfusionRecipes.add(recipe)
    }

    fun removeRecipe(output: ItemStack): Boolean {
        val toRemove = BotaniaAPI.manaInfusionRecipes.filter { it.output != null && it.output.matches(output, true) }
        return when (toRemove.isEmpty()) {
            true -> false
            false -> {
                toRemove.forEach { remove -> BotaniaAPI.manaInfusionRecipes.removeIf { r -> r.output.matches(remove.output, true) } }
                true
            }
        }
    }

}