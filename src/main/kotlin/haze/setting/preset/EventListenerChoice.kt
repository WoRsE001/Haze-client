package haze.setting.preset

import haze.event.Event
import haze.setting.value.ChoiceValue

// Blood! It's everywhere. SCWxD killed you on 18.03.2026 at 13:15.
abstract class EventListenerChoice(name: String, parent: ChoiceValue) : ChoiceValue.SubMode(name, parent) {
    abstract fun onEvent(event: Event)
}