package haze.utility.player

import haze.event.impl.MovementEvent
import haze.utility.player
import haze.utility.player.rotation.CameraRotation
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Input
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.round
import kotlin.math.sin

fun LocalPlayer.isMoving() = input.moveVector.x != 0f || input.moveVector.y != 0f

fun LocalPlayer.hasXZMotion() = deltaMovement.x != 0.0 && deltaMovement.z != 0.0

fun getYawWithMovement(facingYaw: Float, input: Input): Float {
    val forwards = input.forward && !input.backward
    val backwards = input.backward && !input.forward
    val left = input.left && !input.right
    val right = input.right && !input.left

    var actualYaw = facingYaw
    var forward = 1f

    if (backwards) {
        actualYaw += 180f
        forward = -0.5f
    } else if (forwards) {
        forward = 0.5f
    }

    if (left) {
        actualYaw -= 90f * forward
    }
    if (right) {
        actualYaw += 90f * forward
    }

    return actualYaw
}

fun Vec3.withStrafe(
    speed: Double = hypot(x, z),
    strength: Double = 1.0,
    yaw: Float = getYawWithMovement(player.yRot, player.input.keyPresses)
): Vec3 {
    if (!player.isMoving()) {
        return Vec3(0.0, y, 0.0)
    }

    val prevX = x * (1.0 - strength)
    val prevZ = z * (1.0 - strength)
    val useSpeed = speed * strength

    val angle = Math.toRadians(yaw.toDouble())
    val x = (-sin(angle) * useSpeed) + prevX
    val z = (cos(angle) * useSpeed) + prevZ
    return Vec3(x, y, z)
}

fun direction(rotationYaw: Float, moveForward: Float, moveStrafing: Float): Double {
    var yaw: Float = rotationYaw

    if (moveForward < 0F) yaw += 180f

    var forward = 1f

    if (moveForward < 0) forward = -0.5f
    else if (moveForward > 0) forward = 0.5f

    if (moveStrafing > 0) yaw -= 90f * forward
    if (moveStrafing < 0) yaw += 90f * forward

    return Math.toRadians(yaw.toDouble())
}

fun direction() = direction(player.yRot, player.input.moveVector.x, player.input.moveVector.y)

fun silentMoveFix(event: MovementEvent) {
    val z = event.moveVector.y
    val x = event.moveVector.x

    val deltaYaw = CameraRotation.yaw - player.yRot
    val deltaRad = Math.toRadians(deltaYaw.toDouble())

    val newX = x * cos(deltaRad) - z * sin(deltaRad)
    val newZ = z * cos(deltaRad) + x * sin(deltaRad)

    event.moveVector = Vec2(round(newX.toFloat()), round(newZ.toFloat()))
}
