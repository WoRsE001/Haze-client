package haze.utility.rotation

import haze.utility.math.Vec3f
import haze.utility.math.toDegrees
import haze.utility.mc
import haze.utility.player
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import kotlin.math.atan2
import kotlin.math.pow

fun gcd() = (mc.options.sensitivity().get() * 0.6f + 0.2f).pow(3).toFloat() * 1.2f

fun pitchFromDiff(diff: Vec3f) = atan2(diff.y, diff.horizontalLength()).toDegrees().coerceIn(-90f, 90f)
fun pitchTo(point: Vec3f) = pitchFromDiff(Vec3f(player.eyePosition) - point)

fun yawFromDiff(diff: Vec3f) = Mth.wrapDegrees(atan2(diff.z, diff.x).toDegrees() - 90f)
fun yawTo(point: Vec3f) = yawFromDiff(point - Vec3f(player.eyePosition))

fun rotationTo(point: Vec3f) = Rotation(pitchTo(point), yawTo(point))

fun AABB.expand(xz: Double, y: Double): AABB {
    val xzHalf = xz / 2.0
    val yHalf = y / 2.0

    val center = center

    return AABB(
        center.x - xsize * xzHalf,
        center.y - ysize * yHalf,
        center.z - zsize * xzHalf,
        center.x + xsize * xzHalf,
        center.y + ysize * xzHalf,
        center.z + zsize * xzHalf
    )
}

val LivingEntity.rotation: Rotation
    get() = Rotation(xRot, yRot)

fun LivingEntity.rotate(rotation: Rotation) {
    xRot += rotation.pitch
    yRot += rotation.yaw
}
