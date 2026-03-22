package haze.module.impl.world.scaffold

import haze.utility.player
import haze.utility.player.getYawWithMovement
import haze.utility.player.pick
import haze.utility.player.rotation.Rotation
import haze.utility.player.rotation.rotation
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import kotlin.math.abs

fun getValidPitches(targetBlock: BlockPos, yaw: Float): MutableList<Float> {
    val pitches = mutableListOf<Float>()

    for (pitch in 20..90) {
        val hitResult = player.pick(Rotation(pitch.toFloat(), yaw), 4.5, false)

        if (hitResult is BlockHitResult && hitResult.blockPos == targetBlock && hitResult.direction != Direction.UP) {
            pitches.add(pitch.toFloat())
        }
    }

    return pitches
}

fun getNormalPitch(targetBlock: BlockPos, serverYaw: Float, pitchSortMode: PitchSortMode): Float {
    return pitchSortMode.getPitch(getValidPitches(targetBlock, serverYaw))
}

fun getYaw(cameraYaw: Float, correctWithMovement: Boolean, isTelly: Boolean): Float {
    var yaw = (if (correctWithMovement) getYawWithMovement(cameraYaw, player.input.keyPresses) else cameraYaw) + 180

    if (isTelly)
        yaw -= 180

    return yaw
}

fun getNearestRotation(targetBlock: BlockPos, needYaw: Float, pitchSortMode: PitchSortMode): Rotation {
    val rotations = mutableListOf<Rotation>()

    for (i in -180..180) {
        val yaw = i.toFloat()
        val pitch = getNormalPitch(targetBlock, yaw, pitchSortMode)
        val hitResult = player.pick(Rotation(pitch, yaw), 4.5, false)
        if (hitResult is BlockHitResult && hitResult.type == HitResult.Type.BLOCK && hitResult.blockPos == targetBlock)
            rotations.add(Rotation(pitch, yaw))
    }

    if (rotations.isEmpty())
        return player.rotation

    rotations.sortBy { abs(Mth.wrapDegrees(needYaw - it.yaw)) }

    return rotations[0]
}
