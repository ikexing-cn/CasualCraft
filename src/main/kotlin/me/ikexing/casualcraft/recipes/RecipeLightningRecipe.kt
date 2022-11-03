package me.ikexing.casualcraft.recipes

import net.minecraft.item.ItemStack

class RecipeLightningRecipe(
    output: ItemStack, input: List<List<ItemStack>>, chance: Double, priority: Int
) : RecipeBaseTransform(output, input, chance, priority) {}