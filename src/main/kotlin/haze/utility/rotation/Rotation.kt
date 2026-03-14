package haze.utility.rotation

import haze.utility.math.Vec2f
import haze.utility.math.Vec3f
import haze.utility.math.roundTo
import net.minecraft.util.Mth
import net.minecraft.world.phys.AABB
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.max

// created by dicves_recode on 30.11.2025
open class Rotation(var pitch: Float, var yaw: Float) {
    operator fun plus(rotation: Rotation) = Rotation(pitch + rotation.pitch, Mth.wrapDegrees(yaw + rotation.yaw))
    operator fun plusAssign(rotation: Rotation) {
        pitch += rotation.pitch
        yaw = Mth.wrapDegrees(yaw + rotation.yaw)
    }

    operator fun minus(rotation: Rotation) = Rotation(pitch - rotation.pitch, Mth.wrapDegrees(yaw - rotation.yaw))
    operator fun minusAssign(rotation: Rotation) {
        pitch -= rotation.pitch
        yaw = Mth.wrapDegrees(yaw - rotation.yaw)
    }

    operator fun times(rotation: Rotation) = Rotation(pitch * rotation.pitch, Mth.wrapDegrees(yaw * rotation.yaw))
    operator fun timesAssign(rotation: Rotation) {
        pitch *= rotation.pitch
        yaw = Mth.wrapDegrees(yaw * rotation.yaw)
    }

    operator fun times(factor: Float) = Rotation(pitch * factor, Mth.wrapDegrees(yaw * factor))
    operator fun timesAssign(factor: Float) {
        pitch *= factor
        yaw = Mth.wrapDegrees(yaw + factor)
    }

    operator fun div(rotation: Rotation) = Rotation(pitch / rotation.pitch, Mth.wrapDegrees(yaw / rotation.yaw))
    operator fun divAssign(rotation: Rotation) {
        pitch /= rotation.pitch
        yaw = Mth.wrapDegrees(yaw / rotation.yaw)
    }

    operator fun div(factor: Float) = Rotation(pitch / factor, Mth.wrapDegrees(yaw / factor))
    operator fun divAssign(factor: Float) {
        pitch /= factor
        yaw = Mth.wrapDegrees(yaw + factor)
    }

    operator fun unaryPlus() = Rotation(+pitch, +yaw)
    operator fun unaryMinus() = Rotation(-pitch, -yaw)

    fun copy() = Rotation(pitch, yaw)

    fun length() = hypot(pitch, yaw)

    fun wrapped() = Rotation(pitch, Mth.wrapDegrees(yaw))

    fun fixed() = Rotation(pitch.roundTo(gcd()), yaw.roundTo(gcd()))

    fun fix() {
        pitch = pitch.roundTo(gcd())
        yaw = yaw.roundTo(gcd())
    }

    fun singedNormalized(): Rotation {
        val maxAbs = max(abs(pitch), abs(yaw))
        return Rotation(pitch / maxAbs, yaw / maxAbs)
    }

    fun unsingedNormalized(): Rotation {
        val maxAbs = max(abs(pitch), abs(yaw))
        return Rotation(abs(pitch) / maxAbs, abs(yaw) / maxAbs)
    }

    fun limited(rotation: Rotation) =
        limited(rotation.pitch, rotation.yaw)

    fun limited(pitch: Float, yaw: Float) = Rotation(
        this.pitch.coerceIn(-pitch..pitch),
        this.yaw.coerceIn(-yaw..yaw)
    )

    fun limit(rotation: Rotation) =
        limit(rotation.pitch, rotation.yaw)

    fun limit(pitch: Float, yaw: Float) {
        this.pitch = this.pitch.coerceIn(-pitch..pitch)
        this.yaw = this.yaw.coerceIn(-yaw..yaw)
    }

    fun mixed(rotation: Rotation, factor: Rotation): Rotation {
        val invertDelta = Rotation(1f, 1f) - factor

        return Rotation(
            pitch * invertDelta.pitch + rotation.pitch * factor.pitch,
            yaw * invertDelta.yaw + rotation.yaw * factor.yaw
        )
    }

    fun asVec() = Vec2f(pitch, yaw)

    fun abs() = Rotation(abs(pitch), abs(yaw))

    fun coerceIn(box: AABB): Rotation {
        val vectors = arrayOf(
            Vec3f(box.minX, box.minY, box.minZ),
            Vec3f(box.maxX, box.minY, box.minZ),
            Vec3f(box.minX, box.minY, box.maxZ),
            Vec3f(box.maxX, box.minY, box.maxZ),
            Vec3f(box.minX, box.maxY, box.minZ),
            Vec3f(box.maxX, box.maxY, box.minZ),
            Vec3f(box.minX, box.maxY, box.maxZ),
            Vec3f(box.maxX, box.maxY, box.maxZ)
        )

        val rotations = vectors.map { rotationTo(it) }

        val minX = rotations.minBy { it.pitch }.pitch
        val maxX = rotations.maxBy { it.pitch }.pitch
        val minY = rotations.minBy { it.yaw }.yaw
        val maxY = rotations.maxBy { it.yaw }.yaw

        return Rotation(
            pitch.coerceIn(minX, maxX),
            wrapped().yaw.coerceIn(minY, maxY)
        )
    }

    companion object {
        val ZERO = Rotation(0f, 0f)
    }
}