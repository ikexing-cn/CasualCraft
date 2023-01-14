package me.ikexing.casualcraft.events

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorldEventListener
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge

class CCWorldEventListener : IWorldEventListener {

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
        MinecraftForge.EVENT_BUS.post(CCEntityRemoveEvent(entityIn))
    }

    override fun broadcastSound(soundID: Int, pos: BlockPos, data: Int) {}

    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun playEvent(player: EntityPlayer?, type: Int, blockPosIn: BlockPos, data: Int) {
    }

    override fun sendBlockBreakProgress(breakerId: Int, pos: BlockPos, progress: Int) {}
}