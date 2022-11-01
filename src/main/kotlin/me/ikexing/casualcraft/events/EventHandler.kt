package me.ikexing.casualcraft.events

import me.ikexing.casualcraft.recipes.RecipeLightningTransform
import me.ikexing.casualcraft.utils.setCountAndReturnThis
import net.minecraft.entity.item.EntityItem
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*
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
        val recipe = RecipeLightningTransform.matches(entities.map { it.item }, false) ?: return

        recipe.let {
            if (Random.nextDouble() <= it.chance) {
                var outputCount = 0
                while (Objects.nonNull(RecipeLightningTransform.matches(entities.map { e -> e.item }, true))) {
                    outputCount += it.output.count
                }

                val copy = it.output.copy().setCountAndReturnThis(outputCount.coerceAtMost(64))
                val output = EntityItem(world, entity.posX, entity.posY, entity.posZ, copy)
                entities.forEach(EntityItem::setDead)
                output.setEntityInvulnerable(true)
                world.spawnEntity(output)
            }
        }
    }

}