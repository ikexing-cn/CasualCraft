package me.ikexing.casualcraft.recipes

import me.ikexing.casualcraft.utils.matches
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

open class RecipeBaseTransform(
    private val output: ItemStack,
    private val input: List<List<ItemStack>>,
    private val chance: Double,
    val priority: Int
) {

    fun matches(input: List<ItemStack>, consume: Boolean): List<ItemStack> {
        return input.filter { inputStack ->
            this.input.any {
                it.any { recipeInputStack ->
                    val matches = inputStack.matches(recipeInputStack, false)
                        && inputStack.count >= recipeInputStack.count
                    if (matches && consume) inputStack.shrink(recipeInputStack.count)
                    matches
                }
            }
        }
    }

    fun resultMatches(input: List<ItemStack>, consume: Boolean): Boolean {
        return matches(input, consume).size >= input.size
    }

    open fun spawnOutput(pos: BlockPos, entities: List<EntityItem>, world: World, dead: Boolean, outputCount: Int) {
        val copy = this.output.copy().splitStack(outputCount.coerceAtMost(64))
        val output = EntityItem(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), copy)
        if (dead) entities.forEach(EntityItem::setDead)
        output.setEntityInvulnerable(true)
        world.spawnEntity(output)
    }

    companion object {

        private val transformsRecipes: MutableList<RecipeBaseTransform> = mutableListOf()

        fun addRecipe(recipe: RecipeBaseTransform) {
            transformsRecipes.add(recipe)
        }

        private fun <T : RecipeBaseTransform> sortedRecipes(recipeType: Class<T>): List<RecipeBaseTransform> {
            return transformsRecipes.filter { it::class.java == recipeType }
                .sortedByDescending { it.priority }
                .sortedByDescending { it.input.size }
        }

        fun matchesLightning(input: List<ItemStack>, consume: Boolean): RecipeLightningRecipe? {
            return sortedRecipes(RecipeLightningRecipe::class.java).firstOrNull {
                it.resultMatches(input, consume)
            } as RecipeLightningRecipe?
        }

        fun matchesFallBlock(block: IBlockState, input: List<ItemStack>, consume: Boolean): RecipeFallingBlockTransform? {
            return sortedRecipes(RecipeFallingBlockTransform::class.java).firstOrNull {
                (it as RecipeFallingBlockTransform).matches(block, input, consume)
            } as RecipeFallingBlockTransform?
        }

    }

}