package haze.utility.math

// Blood! It's everywhere. SCWxD killed you on 06.03.2026 at 11:55.
class Rect(var lt: Vec2f, var size: Vec2f) {
    constructor(x: Float, y: Float, width: Float, height: Float) : this(Vec2f(x, y), Vec2f(width, height))

    var ct: Vec2f
        get() = Vec2f(lt.x + size.x / 2, lt.y)
        set(value: Vec2f) {
            lt = Vec2f(value.x - size.x / 2, lt.y)
        }

    var rt: Vec2f
        get() = Vec2f(lt.x + size.x, lt.y)
        set(value: Vec2f) {
            lt = Vec2f(value.x - size.x, lt.y)
        }

    var rc: Vec2f
        get() = Vec2f(lt.x + size.x, lt.y + size.y / 2)
        set(value: Vec2f) {
            lt = Vec2f(value.x - size.x, lt.y - size.y / 2)
        }

    var rb: Vec2f
        get() = lt + size
        set(value: Vec2f) {
            lt = value - size
        }

    var cb: Vec2f
        get() = Vec2f(lt.x + size.x / 2, lt.y + size.y)
        set(value: Vec2f) {
            lt = Vec2f(value.x - size.x / 2, lt.y - size.y)
        }

    var lb: Vec2f
        get() = Vec2f(lt.x, lt.y + size.y)
        set(value: Vec2f) {
            lt = Vec2f(value.x, lt.y - size.y)
        }

    var lc: Vec2f
        get() = Vec2f(lt.x, lt.y + size.y / 2)
        set(value: Vec2f) {
            lt = Vec2f(value.x, lt.y - size.y / 2)
        }

    var center: Vec2f
        get() = lt + size / 2f
        set(value: Vec2f) {
            lt = value - size / 2f
        }

    fun isCollided(pointX: Float, pointY: Float, x: Float, y: Float, width: Float, height: Float) = pointX in x..(x + width) && pointY in y.. (y + height)
    fun isCollided(pointX: Float, pointY: Float) = isCollided(pointX, pointY, lt.x, lt.y, size.x, size.y)
}