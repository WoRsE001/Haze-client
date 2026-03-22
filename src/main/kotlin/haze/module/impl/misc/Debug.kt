package haze.module.impl.misc

import haze.event.Event
import haze.event.impl.PacketEvent
import haze.event.impl.TickEvent
import haze.module.Category
import haze.module.Module
import haze.utility.mc
import haze.utility.player
import haze.utility.sendMessage
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket

// Blood! It's everywhere. SCWxD killed you on 09.03.2026 at 11:48.
object Debug : Module("Debug", Category.MISC) {
    private val flagDetector by boolean("Flag detector", false)
    private val cpsCounter by boolean("CPS counter", false)

    val CPSArray = Array(10) { 0 }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            CPSArray[player.tickCount % 10] = 0
        }
        if (event is TickEvent.Post) {
            mc.sendMessage((CPSArray.sum()).toString())
        }

        if (event is PacketEvent.Receive) {
            val packet = event.packet
            if (flagDetector && packet is ClientboundPlayerPositionPacket) {
                mc.sendMessage("Flag detect")
            }
        }
    }
}