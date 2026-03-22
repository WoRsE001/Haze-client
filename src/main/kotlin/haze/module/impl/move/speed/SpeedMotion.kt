package haze.module.impl.move.speed

import haze.event.Event
import haze.event.impl.TickEvent
import haze.setting.preset.EventListenerChoice
import haze.setting.value.ChoiceValue
import haze.utility.player
import haze.utility.player.withStrafe
import net.minecraft.world.phys.Vec3

// Blood! It's everywhere. SCWxD killed you on 18.03.2026 at 10:36.
class SpeedMotion(parent: ChoiceValue) : EventListenerChoice("Motion", parent) {
    private val speedXZ by number("Speed", 1.0, 0.0..10.0, 0.1)

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            player.deltaMovement = player.deltaMovement.multiply(Vec3(0.0, 1.0, 0.0))
            player.deltaMovement = player.deltaMovement.withStrafe(speedXZ)
        }
    }
}