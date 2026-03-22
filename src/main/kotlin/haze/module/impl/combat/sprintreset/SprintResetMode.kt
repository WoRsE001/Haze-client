package haze.module.impl.combat.sprintreset

import haze.event.Event
import haze.event.impl.TickEvent
import haze.setting.value.ChoiceValue

// created by dicves_recode on 29.12.2025
abstract class SprintResetMode(name: String, parent: ChoiceValue) : ChoiceValue.SubMode(name, parent) {
    open fun startReset(event: Event): Boolean { return true }
    open fun reset(event: Event): Boolean { return event is TickEvent.Pre }
    open fun stopReset(event: Event): Boolean { return true }
}
