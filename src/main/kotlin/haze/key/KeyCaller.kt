package haze.key

import haze.event.Event
import haze.event.EventListener
import haze.event.impl.KeyEvent
import haze.utility.mc

// created by dicves_recode on 28.11.2025
object KeyCaller : EventListener {
    init {
        registerToEvents()
    }

    private val listeners = mutableSetOf<KeyListener>()

    internal fun register(listener: KeyListener) {
        listeners.add(listener)
    }

    override fun onEvent(event: Event) {
        if (event is KeyEvent) {
            for (listener in listeners) {
                if (listener.shouldListenKeys())
                    continue

                val keybind = listener.keybind

                if (keybind == Keybind.NONE)
                    continue

                listener.onKey(event.action)
            }
        }
    }

    override fun shouldListenEvents() = mc.player != null && mc.level != null && mc.screen == null
}
