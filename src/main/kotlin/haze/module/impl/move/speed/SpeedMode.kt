package haze.module.impl.move.speed

import haze.event.Event
import haze.setting.value.ChoiceValue

// Blood! It's everywhere. SCWxD killed you on 18.03.2026 at 10:29.
abstract class SpeedMode(name: String, parent: ChoiceValue) : ChoiceValue.SubMode(name, parent) {
    abstract fun onEvent(event: Event)
}