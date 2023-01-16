package me.ikexing.casualcraft.recipes

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class RecipeFallingBlockTransform(
    private val block: IBlockState, private val exactMatch: Boolean,
    private val output: ItemStack, input: List<List<ItemStack>>, chance: Double, priority: Int
) : RecipeBaseTransform(output, input, chance, priority) {

    init {
        blocks.add(block)
    }

    fun spawnOutput(block: IBlockState, pos: BlockPos, entities: List<EntityItem>, world: World, dead: Boolean) {
        var outputCount = 0
        while (Objects.nonNull(matchesFallBlock(block, entities.map { it.item }, false))) {
            matchesFallBlock(block, entities.map { it.item }, true)
            outputCount += output.count
        }

        super.spawnOutput(pos, entities, world, dead, outputCount)
    }

    fun matches(block: IBlockState, input: List<ItemStack>, consume: Boolean): Boolean {
        return if (!exactMatch) this.block == block else this.block.block == block.block
            && super.resultMatches(input, consume)
    }

    companion object {
        val blocks = mutableListOf<IBlockState>()
    }

}