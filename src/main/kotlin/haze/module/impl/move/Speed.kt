package haze.module.impl.move

import haze.event.Event
import haze.module.Category
import haze.module.Module
import haze.module.impl.move.speed.*
import haze.setting.preset.EventListenerChoice

// испорченно SCWGxD в 15:04 27.12.2025
object Speed : Module("Speed", Category.MOVE) {
    private val mode = list("Mode").apply {
        choice(SpeedMotion)
        choice(SpeedEntityCollide).select()
    }

    override fun onEvent(event: Event) {
        val selectedMode = mode.get()

        if (selectedMode is EventListenerChoice)
            selectedMode.onEvent(event)
    }
}
