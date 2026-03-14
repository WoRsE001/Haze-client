package haze.utility.math

import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.sqrt

// Blood! It's everywhere. SCWxD killed you on 09.03.2026 at 6:31.
class Vec3f(var x: Float, var y: Float, var z: Float) {
    constructor(x: Double, y: Double, z: Double) : this(x.toFloat(), y.toFloat(), z.toFloat())
    constructor(vec3: Vec3) : this(vec3.x, vec3.y, vec3.z)

    operator fun plus(another: Vec3f) = Vec3f(x + another.x, y + another.y, z + another.z)
    operator fun plusAssign(another: Vec3f) {
        x += another.x
        y += another.y
        z += another.z
    }

    operator fun minus(another: Vec3f) = Vec3f(x - another.x, y - another.y, z - another.z)
    operator fun minusAssign(another: Vec3f) {
        x -= another.x
        y -= another.y
        z -= another.z
    }

    operator fun times(another: Vec3f) = Vec3f(x * another.x, y * another.y, z * another.z)
    operator fun timesAssign(another: Vec3f) {
        x *= another.x
        y *= another.y
        z *= another.z
    }

    operator fun times(factor: Float) = Vec3f(x * factor, y * factor, z * factor)
    operator fun timesAssign(factor: Float) {
        x *= factor
        y += factor
        z *= factor
    }

    operator fun div(another: Vec3f) = Vec3f(x / another.x, y / another.y, z / another.z)
    operator fun divAssign(another: Vec3f) {
        x /= another.x
        y /= another.y
        z /= another.z
    }

    operator fun div(factor: Float) = Vec3f(x / factor, y / factor, z / factor)
    operator fun divAssign(factor: Float) {
        x /= factor
        y /= factor
        z /= factor
    }

    fun length() = sqrt(x * x + y * y + z * z)
    fun horizontalLength() = sqrt(x * x + z * z)

    fun normalize() {
        x /= length()
        y /= length()
        z /= length()
    }

    fun normalized() = this / length()

    fun coerceIn(box: AABB) = Vec3f(
        x.coerceIn(box.minX.toFloat()..box.maxX.toFloat()),
        y.coerceIn(box.minY.toFloat()..box.maxY.toFloat()),
        z.coerceIn(box.minZ.toFloat()..box.maxZ.toFloat())
    )
}