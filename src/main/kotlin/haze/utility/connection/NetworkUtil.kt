package haze.utility.connection

import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.network.protocol.Packet

// Blood! It's everywhere. SCWxD killed you on 22.03.2026 at 6:23.
fun ClientPacketListener.sendInvisiblePacket(packet: Packet<*>) {
    this.connection.send(packet, null)
}