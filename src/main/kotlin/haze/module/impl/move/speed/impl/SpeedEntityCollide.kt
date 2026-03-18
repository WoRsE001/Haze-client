package haze.module.impl.move.speed.impl

import haze.event.Event
import haze.event.impl.TickEvent
import haze.module.impl.move.speed.SpeedMode
import haze.setting.value.ChoiceValue
import haze.utility.mc
import haze.utility.sendMessage

// Blood! It's everywhere. SCWxD killed you on 18.03.2026 at 10:43.
class SpeedEntityCollide(parent: ChoiceValue) : SpeedMode("Entity collide", parent) {
    private val bool by boolean("Bool", false)

    override fun onEvent(event: Event) {
        if (event is TickEvent.PRE) {
            mc.sendMessage(bool.toString())
        }
    }
}