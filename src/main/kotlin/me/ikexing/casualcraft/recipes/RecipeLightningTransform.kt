package me.ikexing.casualcraft.recipes

import me.ikexing.casualcraft.utils.matches
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

class RecipeLightningTransform(
    val output: ItemStack,
    private val input: List<ItemStack>,
    val chance: Double?
) {

    // FIXME: match no stackable items and recipe priority
    fun matches(input: List<ItemStack>): Boolean {
        return input.count { stack -> this.input.any { it.matches(stack) } } >= this.input.size
    }

    companion object {

        // TODO: recipe priority
        private val lightningTransformRecipes: List<RecipeLightningTransform> = mutableListOf(
            RecipeLightningTransform(
                ItemStack(Items.COOKIE),
                listOf(ItemStack(Items.STONE_AXE), ItemStack(Items.DIAMOND, 2)),
                1.0
            ),
            RecipeLightningTransform(
                ItemStack(Items.DIAMOND),
                listOf(ItemStack(Items.STONE_SWORD)),
                1.0
            )
        )

        fun matches(input: List<ItemStack>): RecipeLightningTransform? {
            if (input.isEmpty()) return null
            return lightningTransformRecipes.firstOrNull { it.matches(input) }
        }

    }

}