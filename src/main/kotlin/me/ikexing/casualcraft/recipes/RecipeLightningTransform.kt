package me.ikexing.casualcraft.recipes

import me.ikexing.casualcraft.utils.matches
import me.ikexing.casualcraft.utils.setCountAndReturnThis
import net.minecraft.item.ItemStack

class RecipeLightningTransform(
    val output: ItemStack,
    private val input: List<List<ItemStack>>,
    val chance: Double,
    private val priority: Int
) {

    fun matches(input: List<ItemStack>, consume: Boolean): Boolean {
        return input.count { inputStack ->
            this.input.any {
                it.any { recipeInputStack ->
                    val copyInputStack = inputStack.copy().setCountAndReturnThis(1)
                    val copyRecipeInputStack = recipeInputStack.copy().setCountAndReturnThis(1)
                    val matches = copyInputStack.matches(copyRecipeInputStack, false)
                            && inputStack.count >= recipeInputStack.count
                    if (matches && consume) inputStack.shrink(recipeInputStack.count)
                    matches
                }
            }
        } >= this.input.size
    }

    companion object {

        private var lightningTransformRecipes: MutableList<RecipeLightningTransform> = mutableListOf()

        fun addRecipe(output: ItemStack, input: List<List<ItemStack>>, change: Double, priority: Int) {
            lightningTransformRecipes.add(RecipeLightningTransform(output, input, change, priority))
        }

        fun matches(input: List<ItemStack>, consume: Boolean): RecipeLightningTransform? {
            return lightningTransformRecipes.sortedByDescending { it.priority }
                .sortedByDescending { it.input.size }
                .firstOrNull { it.matches(input, consume) }
        }

    }

}