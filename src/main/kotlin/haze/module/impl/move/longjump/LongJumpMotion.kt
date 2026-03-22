package haze.module.impl.move.longjump

import haze.event.Event
import haze.event.impl.JumpEvent
import haze.setting.preset.EventListenerChoice
import haze.setting.value.ChoiceValue
import haze.utility.player
import haze.utility.player.withStrafe
import net.minecraft.world.phys.Vec3

// Blood! It's everywhere. SCWxD killed you on 18.03.2026 at 13:18.
object LongJumpMotion : EventListenerChoice("Motion") {
    private val motion by number("Motion", 1.0, 0.0..10.0, 0.01)

    override fun onEvent(event: Event) {
        if (event is JumpEvent.Post) {
            player.deltaMovement = player.deltaMovement.multiply(Vec3(0.0, 1.0, 0.0))
            player.deltaMovement = player.deltaMovement.withStrafe(motion)
        }
    }
}