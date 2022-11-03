package me.ikexing.casualcraft.recipes

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack

class RecipeFallingBlockTransform(
    private val block: IBlockState, private val exactMatch: Boolean,
    output: ItemStack, input: List<List<ItemStack>>, chance: Double, priority: Int
) : RecipeBaseTransform(output, input, chance, priority) {

    init {
        blocks.add(block)
    }

    fun matches(block: IBlockState, input: List<ItemStack>, consume: Boolean): Boolean {
        return if (exactMatch) this.block == block else this.block.block == block.block &&
                super.matches(input, consume)
    }

    companion object {
        val blocks = mutableListOf<IBlockState>()
    }

}