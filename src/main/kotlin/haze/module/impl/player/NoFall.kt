package haze.module.impl.player

import haze.event.Event
import haze.event.impl.SendPosEvent
import haze.module.Category
import haze.module.Module
import haze.utility.player

// испорченно SCWGxD в 27.12.2025:19:42
object NoFall : Module("NoFall", Category.PLAYER) {
    private val mode = list("Mode")

    private val modeSpoofGround = mode.choice("Spoof ground")
    private val onGround by boolean("On ground", true).visible { modeSpoofGround.selected() }

    override fun onEvent(event: Event) {
        if (event is SendPosEvent.Pre) {
            if (modeSpoofGround.selected()) {
                player.setOnGround(onGround)
            }
        }
    }
}