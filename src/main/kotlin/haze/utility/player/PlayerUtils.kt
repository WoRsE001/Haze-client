package haze.utility.player

import haze.utility.mc
import haze.utility.player
import haze.utility.rotation.Rotation
import haze.utility.level
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.protocol.Packet
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

// испорченно SCWGxD в 20.12.2025:21:28
fun LocalPlayer.pick(rotation: Rotation, range: Double, stopOnFluid: Boolean): HitResult {
    val vec3 = eyePosition
    val vec32 = calculateViewVector(rotation.pitch, rotation.yaw)
    val vec33 = vec3.add(vec32.x * range, vec32.y * range, vec32.z * range)
    return level.clip(
        ClipContext(
            vec3,
            vec33,
            ClipContext.Block.OUTLINE,
            if (stopOnFluid) ClipContext.Fluid.ANY else ClipContext.Fluid.NONE,
            this
        )
    )
}

fun canSeePointFrom(
    eyes: Vec3,
    vec3: Vec3,
) = level.clip(
    ClipContext(
        eyes, vec3, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player,
    )
).type == HitResult.Type.MISS

fun facingEnemy(
    fromEntity: Entity = mc.cameraEntity!!,
    toEntity: LivingEntity?,
    range: Double,
    wallsRange: Double,
): Boolean {
    val cameraVec = fromEntity.eyePosition
    val rotationVec = player.getViewVector(1f)

    val vec3d3 = cameraVec.add(rotationVec.x * range, rotationVec.y * range, rotationVec.z * range)
    val box = fromEntity.boundingBox.expandTowards(rotationVec.multiply(range, range, range)).expandTowards(1.0, 1.0, 1.0)

    val entityHitResult =
        ProjectileUtil.getEntityHitResult(
            fromEntity, cameraVec, vec3d3, box, { !it.isSpectator && it == toEntity }, range * range,
        ) ?: return false

    val distance = cameraVec.distanceToSqr(entityHitResult.location)

    return distance <= range * range && canSeePointFrom(cameraVec, entityHitResult.location) || distance <= wallsRange * wallsRange
}

fun ClientPacketListener.sendInvisiblePacket(packet: Packet<*>) {
    this.connection.send(packet)
}

var utilAirTicks = 0
var utilGroundTicks = 0

val LocalPlayer.airTicks: Int
    get() = utilAirTicks

val LocalPlayer.groundTicks: Int
    get() = utilGroundTicks

