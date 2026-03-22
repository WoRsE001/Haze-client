package haze.utility.target

import haze.utility.player
import haze.utility.player.rotation.CameraRotation
import haze.utility.player.rotation.yawTo
import haze.utility.level
import haze.utility.math.Vec3f
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand
import kotlin.math.abs

var lastAttackedTarget: LivingEntity? = null

enum class SortType(val sort: (LivingEntity) -> Double) {
    FOV({ abs(CameraRotation.yaw - yawTo(Vec3f(it.eyePosition))).toDouble() }),
    Distance({ player.distanceTo(it).toDouble() }),
    Health({ (it.health + it.absorptionAmount).toDouble() }),
    HurtTime({ it.hurtTime.toDouble() });
}

fun validTargets(
    range: Double
): List<LivingEntity> {
    return level.entitiesForRendering()
        .filterIsInstance<LivingEntity>()
        .filter { it != player && player.distanceTo(it) <= range && !it.isDeadOrDying && it !is ArmorStand }
}

fun List<LivingEntity>.bestTargetBy(
    sortType: SortType
): LivingEntity? {
    return this.minByOrNull { sortType.sort(it) }
}
