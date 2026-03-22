package haze.event.impl

import haze.event.CancelAbleEvent
import haze.event.Event
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

// испорченно SCWGxD в 17.12.2025:15:15
interface AttackEvent {
    data class PRE(val target: Entity) : CancelAbleEvent()
    object POST : Event
}

data class SlowDownEvent(var slowDown: Double, var sprint: Boolean) : Event

interface PlayerStateUpdateEvent {
    object Pre: Event
    object Post: Event
}

object MoveRelativeEvent : Event {
    var yaw = 0f
    lateinit var input: Vec3
}

interface JumpEvent {
    object Pre : CancelAbleEvent() {
        var yaw = 0f
        var height = 0f
    }

    object Post : Event
}

interface SendPosEvent {
    object Pre: Event

    object Post: Event
}