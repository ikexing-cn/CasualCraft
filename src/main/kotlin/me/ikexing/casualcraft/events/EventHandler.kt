package me.ikexing.casualcraft.events

import me.ikexing.casualcraft.recipes.RecipeBaseTransform
import me.ikexing.casualcraft.recipes.RecipeFallingBlockTransform
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.entity.item.EntityItem
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@EventBusSubscriber
object EventHandler {

    @JvmStatic
    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        event.world.addEventListener(CCWorldEventListener())
    }

    @JvmStatic
    @SubscribeEvent
    fun onEntityStruckByLightning(event: EntityStruckByLightningEvent) {
        val entity = event.entity
        val world = entity.world

        if (world.isRemote || entity !is EntityItem) return
        val entities = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(entity.position))
        val recipe = RecipeBaseTransform.matchesLightning(entities.map { it.item }, false) ?: return



        recipe.spawnOutput(entity.position, entities, world, true)
    }

    @JvmStatic
    @SubscribeEvent
    fun onEntityRemove(event: CCEntityRemoveEvent) {
        val entity = event.entity
        val world = entity.world

        if (!world.isRemote) {
            if (entity is EntityFallingBlock) {
                if (world.isAirBlock(entity.position.down())) return
                if (RecipeFallingBlockTransform.blocks.contains(entity.block).not()) return

                val entityItems = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(entity.position))
                val recipe = entity.block?.let { it -> RecipeBaseTransform.matchesFallBlock(it, entityItems.map { it.item }, false) }
                    ?: return
                recipe.spawnOutput(entity.block!!, entity.position, entityItems, world, false)
            }
        }
    }

}