package haze.utility.math

import kotlin.math.cos
import kotlin.math.sin

// Blood! It's everywhere. SCWxD killed you on 06.03.2026 at 11:57.
class Vec2f(var x: Float, var y: Float) {
    operator fun plus(another: Vec2f) = Vec2f(x + another.x, y + another.y)
    operator fun plusAssign(another: Vec2f) {
        x += another.x
        y += another.y
    }

    operator fun minus(another: Vec2f) = Vec2f(x - another.x, y - another.y)
    operator fun minusAssign(another: Vec2f) {
        x -= another.x
        y -= another.y
    }

    operator fun times(another: Vec2f) = Vec2f(x * another.x, y * another.y)
    operator fun timesAssign(another: Vec2f) {
        x *= another.x
        y *= another.y
    }

    operator fun times(factor: Float) = Vec2f(x * factor, y * factor)
    operator fun timesAssign(factor: Float) {
        x *= factor
        y += factor
    }

    operator fun div(another: Vec2f) = Vec2f(x / another.x, y / another.y)
    operator fun divAssign(another: Vec2f) {
        x /= another.x
        y /= another.y
    }

    operator fun div(factor: Float) = Vec2f(x / factor, y / factor)
    operator fun divAssign(factor: Float) {
        x /= factor
        y /= factor
    }

    fun toVec3f(): Vec3f {
        val h = x * (Math.PI / 180.0);
        val i = -y * (Math.PI / 180.0);
        val j = cos(i);
        val k = sin(i);
        val l = cos(h);
        val m = sin(h);
        return Vec3f(k * l, -m, j * l)
    }
}