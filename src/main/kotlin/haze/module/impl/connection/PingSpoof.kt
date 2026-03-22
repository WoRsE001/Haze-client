package haze.module.impl.connection

import haze.event.Event
import haze.module.Category
import haze.module.Module
import haze.utility.connection.PacketDelayer

// Blood! It's everywhere. SCWxD killed you on 14.02.2026 at 4:26.
object PingSpoof : Module("PingSpoof", Category.CONNECT), PacketDelayer {
    override var sentPacketsDelayTime = 0
    override var holdSentPackets = false

    private val sentDelay by number("Sent delay", 600.0, 0.0..10000.0, 1.0)

    init {
        registerToPacketHandler()
    }

    override fun onEvent(event: Event) {
    }

    override fun resetSendDelay() {
        sentPacketsDelayTime = sentDelay.toInt()
    }
}