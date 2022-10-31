package me.ikexing.casualcraft.events

import me.ikexing.casualcraft.recipes.RecipeLightningTransform
import net.minecraft.entity.item.EntityItem
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.random.Random

@EventBusSubscriber
object EventHandler {

    @JvmStatic
    @SubscribeEvent
    fun onEntityStruckByLightning(event: EntityStruckByLightningEvent) {
        val entity = event.entity
        val world = entity.world

        if (world.isRemote || entity !is EntityItem) return
        val entities = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(entity.position))
        val recipe = RecipeLightningTransform.matches(entities.map { it.item }) ?: return

        // FIXME: why was it triggered twice?
        recipe.let {
            if (Random.nextDouble() <= (it.chance ?: 1.0)) {
                val output = EntityItem(world, entity.posX, entity.posY, entity.posZ, it.output.copy())
                output.setEntityInvulnerable(true)
                world.spawnEntity(output)
            }
        }
    }

}