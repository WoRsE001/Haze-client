package haze.utility.player

import haze.utility.level
import haze.utility.mc
import haze.utility.player
import haze.utility.player.rotation.Rotation
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

// Blood! It's everywhere. SCWxD killed you on 22.03.2026 at 6:22.
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

fun canSeePointFrom(eyes: Vec3, vec3: Vec3) =
    level.clip(ClipContext(
        eyes,
        vec3,
        ClipContext.Block.OUTLINE,
        ClipContext.Fluid.NONE,
        player,
    )).type == HitResult.Type.MISS

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