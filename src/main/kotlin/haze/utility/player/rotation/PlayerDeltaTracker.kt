package haze.utility.player.rotation

import haze.event.Event
import haze.event.EventListener
import haze.event.impl.TickEvent
import haze.utility.mc
import haze.utility.player

// created by dicves_recode on 02.01.2026
object PlayerDeltaTracker : EventListener {
    private var lastRotation = Rotation.ZERO.copy()
    var delta = Rotation.ZERO.copy()

    init {
        registerToEvents()
    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            val playerRotation = player.rotation

            delta = (playerRotation - lastRotation).wrapped()
            lastRotation = playerRotation
        }
    }

    override fun shouldListenEvents() = mc.player != null && mc.level != null
}
