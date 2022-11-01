package me.ikexing.casualcraft.recipes

import me.ikexing.casualcraft.utils.matches
import net.minecraft.item.ItemStack

class RecipeLightningTransform(
    val output: ItemStack,
    private val input: List<List<ItemStack>>,
    val chance: Double,
    private val priority: Int
) {

    fun matches(input: List<ItemStack>, consume: Boolean): Boolean {
        return input.count { stack -> this.input.any { it.any { item ->
            val matches = item.matches(stack)
            if (matches && consume) stack.shrink(item.count)
            matches
        } } } >= this.input.size
    }

    companion object {

        private var lightningTransformRecipes: MutableList<RecipeLightningTransform> = mutableListOf()

        fun addRecipe(output: ItemStack, input: List<List<ItemStack>>, change: Double, priority: Int) {
            lightningTransformRecipes.add(RecipeLightningTransform(output, input, change, priority))
        }

        fun matches(input: List<ItemStack>, consume: Boolean): RecipeLightningTransform? {
            return lightningTransformRecipes.sortedBy { it.priority }.firstOrNull { it.matches(input, consume) }
        }

    }

}