package haze.module.impl.move

import haze.event.Event
import haze.module.Category
import haze.module.Module
import haze.module.impl.move.longjump.LongJumpMotion
import haze.setting.preset.EventListenerChoice

// i regret everything, i've done, SCWGxD (06.02.2026:15:48)
object LongJump : Module(
    "LongJump",
    Category.MOVE
) {
    private val modes = list("Modes")
    private val modeMotion by LongJumpMotion(modes)

    override fun onEvent(event: Event) {
        val mode = modes.get()
        if (mode !is EventListenerChoice) return
        mode.onEvent(event)
    }
}