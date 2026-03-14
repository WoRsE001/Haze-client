package haze.event.impl

import haze.event.Event
import net.minecraft.world.phys.Vec2

// created by dicves_recode on 30.11.2025
object CursorDeltaEvent : Event {
    var accumulatedDX = 0.0
    var accumulatedDY = 0.0
}

data class KeyEvent(val key: Int, val action: Int, val mods: Int) : Event

object MovementEvent : Event {
    var moveVector = Vec2(0f, 0f)
}

object MovementInputEvent : Event {
    var forward = false
    var back = false
    var left = false
    var right = false
    var jump = false
    var sneak = false
    var sprint = false
}
