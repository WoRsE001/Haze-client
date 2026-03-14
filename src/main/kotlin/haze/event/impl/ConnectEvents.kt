package haze.event.impl

import haze.event.CancelAbleEvent
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ServerboundPongPacket

interface PacketEvent {
    object SEND : CancelAbleEvent() {
        var packet: Packet<*> = ServerboundPongPacket(0) // заглушка
        var totalDelay = 0

        fun addDelay(delay: Int) {
            totalDelay += delay
        }
    }

    data class RECEIVE(val packet: Packet<*>) : CancelAbleEvent()
}