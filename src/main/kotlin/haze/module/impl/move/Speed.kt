package haze.module.impl.move

import haze.event.Event
import haze.module.Category
import haze.module.Module
import haze.module.impl.move.speed.SpeedMode
import haze.module.impl.move.speed.impl.SpeedEntityCollide
import haze.module.impl.move.speed.impl.SpeedMotion

// испорченно SCWGxD в 15:04 27.12.2025
object Speed : Module("Speed", Category.MOVE) {
    private val modes = list("Mode")
    private val modeEntityCollide = SpeedEntityCollide(modes).select()
    private val modeMotion = SpeedMotion(modes).select()

    override fun onEvent(event: Event) {
        val mode = modes.get()
        if (mode !is SpeedMode) return
        mode.onEvent(event)
    }
}
