package haze.module.impl.player

import haze.event.Event
import haze.event.impl.GameLoopEvent
import haze.module.Category
import haze.module.Module
import haze.utility.mc
import haze.utility.timer

// испорченно SCWGxD в 02.01.2026:14:34
object Timer : Module(
    "Timer",
    Category.PLAYER
) {
    val timer by number("Timer", 1.0, 0.01..10.0, 0.01)

    override fun onEvent(event: Event) {
        if (event is GameLoopEvent) {
            mc.timer = timer.toFloat()
        }
    }
}