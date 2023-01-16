package me.ikexing.casualcraft.recipes

import me.ikexing.casualcraft.core.utils.reflect.ReflectEntityItem
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class RecipeEntityCloudRecipe(
    private val potions: List<PotionEffect>, private val output: ItemStack,
    input: List<List<ItemStack>>, chance: Double, priority: Int
) : RecipeBaseTransform(output, input, chance, priority) {

    init {
        items.addAll(input)
    }

    fun matches(input: List<EntityItem>, consume: Boolean): Boolean {
        val inputItems = input.map { it.item }
        return super.resultMatches(inputItems, consume) && potionMatches(input)
    }

    fun spawnOutput(pos: BlockPos, entities: List<EntityItem>, world: World, dead: Boolean) {
        var outputCount = 0
        while (Objects.nonNull(matchesEntityCloudRecipe(entities, false))) {
            matchesEntityCloudRecipe(entities, true)
            outputCount += output.count
        }

        super.spawnOutput(pos, entities, world, dead, outputCount)
    }

    private fun potionMatches(input: List<EntityItem>): Boolean {
        return input.all { entity ->
            this.potions.all { potion ->
                val potions = ReflectEntityItem.getPotions(entity)
                if (potions == null || potions.isEmpty()) return false

                potions.any { entityPotion ->
                    entityPotion.key === potion.effectName
                        && entityPotion.value.key >= potion.amplifier
                        && entityPotion.value.value >= potion.duration
                }
            }
        }
    }

    companion object {
        val items = mutableListOf<List<ItemStack>>()
    }

}