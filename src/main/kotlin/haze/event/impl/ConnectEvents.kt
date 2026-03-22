package haze.event.impl

import haze.event.CancelAbleEvent
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.ServerboundPongPacket

interface PacketEvent {
    object Send : CancelAbleEvent() {
        var packet: Packet<*> = ServerboundPongPacket(0) // заглушка
    }

    data class Receive(val packet: Packet<*>) : CancelAbleEvent()
}