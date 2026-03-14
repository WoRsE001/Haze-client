package haze.module.impl.connection

import haze.event.Event
import haze.event.impl.PacketEvent
import haze.module.Category
import haze.module.Module

// Blood! It's everywhere. SCWxD killed you on 14.02.2026 at 4:26.
object FakePing : Module("FakePing", Category.CONNECT) {
    override fun onEvent(event: Event) {
        if (event is PacketEvent.SEND) {
            event.addDelay(600)
        }
    }
}