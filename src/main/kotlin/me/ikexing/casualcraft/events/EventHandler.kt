package me.ikexing.casualcraft.events

import me.ikexing.casualcraft.recipes.RecipeBaseTransform
import me.ikexing.casualcraft.recipes.RecipeFallingBlockTransform
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.entity.item.EntityItem
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent

@EventBusSubscriber
object EventHandler {

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
    fun onWorldTick(event: WorldTickEvent) {
        if (event.side.isServer && event.phase === Phase.END) {
            val world = event.world
            val fallingBlockEntities = world.loadedEntityList.filterIsInstance<EntityFallingBlock>()
            for (fallingBlock in fallingBlockEntities) {
                if (world.isAirBlock(fallingBlock.position.down())) continue
                if (RecipeFallingBlockTransform.blocks.contains(fallingBlock.block).not()) continue

                val entityItems = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(fallingBlock.position))
                val recipe = fallingBlock.block?.let { it -> RecipeBaseTransform.matchesFallBlock(it, entityItems.map { it.item }, false) } ?: continue
                recipe.spawnOutput(fallingBlock.position, entityItems, world, false)
            }
        }
    }

}