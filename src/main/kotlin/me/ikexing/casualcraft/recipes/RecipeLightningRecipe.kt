package me.ikexing.casualcraft.recipes

import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class RecipeLightningRecipe(
    private val output: ItemStack, input: List<List<ItemStack>>, chance: Double, priority: Int
) : RecipeBaseTransform(output, input, chance, priority) {

    fun spawnOutput(pos: BlockPos, entities: List<EntityItem>, world: World, dead: Boolean) {
        var outputCount = 0
        while (Objects.nonNull(matchesLightning(entities.map { it.item }, false))) {
            matchesLightning(entities.map { it.item }, true)
            outputCount += output.count
        }

        super.spawnOutput(pos, entities, world, dead, outputCount)
    }

}