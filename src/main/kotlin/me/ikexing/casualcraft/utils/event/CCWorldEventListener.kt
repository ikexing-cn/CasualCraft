package me.ikexing.casualcraft.utils.event

import me.ikexing.casualcraft.recipes.RecipeBaseTransform
import me.ikexing.casualcraft.recipes.RecipeFallingBlockTransform
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorldEventListener
import net.minecraft.world.World

class CCWorldEventListener(private val worldIn: World) : IWorldEventListener {

    override fun notifyBlockUpdate(worldIn: World, pos: BlockPos, oldState: IBlockState, newState: IBlockState, flags: Int) {}

    override fun notifyLightSet(pos: BlockPos) {}

    override fun markBlockRangeForRenderUpdate(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) {}

    override fun playSoundToAllNearExcept(
        player: EntityPlayer?,
        soundIn: SoundEvent,
        category: SoundCategory,
        x: Double,
        y: Double,
        z: Double,
        volume: Float,
        pitch: Float
    ) {
    }

    override fun playRecord(soundIn: SoundEvent, pos: BlockPos) {}

    override fun spawnParticle(
        particleID: Int,
        ignoreRange: Boolean,
        xCoord: Double,
        yCoord: Double,
        zCoord: Double,
        xSpeed: Double,
        ySpeed: Double,
        zSpeed: Double,
        vararg parameters: Int
    ) {
    }

    override fun spawnParticle(
        id: Int,
        ignoreRange: Boolean,
        minimiseParticleLevel: Boolean,
        x: Double,
        y: Double,
        z: Double,
        xSpeed: Double,
        ySpeed: Double,
        zSpeed: Double,
        vararg parameters: Int
    ) {
    }

    override fun onEntityAdded(entityIn: Entity) {}

    override fun onEntityRemoved(entityIn: Entity) {
        if (!worldIn.isRemote && entityIn is EntityFallingBlock) {
            if (worldIn.isAirBlock(entityIn.position.down())) return
            if (RecipeFallingBlockTransform.blocks.contains(entityIn.block).not()) return

            val entityItems = worldIn.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(entityIn.position))
            val recipe = entityIn.block?.let { it -> RecipeBaseTransform.matchesFallBlock(it, entityItems.map { it.item }, false) } ?: return
            recipe.spawnOutput(entityIn.position, entityItems, worldIn, false)
        }
    }

    override fun broadcastSound(soundID: Int, pos: BlockPos, data: Int) {}

    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun playEvent(player: EntityPlayer?, type: Int, blockPosIn: BlockPos, data: Int) {
    }

    override fun sendBlockBreakProgress(breakerId: Int, pos: BlockPos, progress: Int) {}
}