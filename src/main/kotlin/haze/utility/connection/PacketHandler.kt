package haze.utility.connection

import com.google.common.collect.Queues
import haze.utility.connection
import net.minecraft.network.protocol.Packet

// Blood! It's everywhere. SCWxD killed you on 14.02.2026 at 4:18.
object PacketHandler {
    val packetDelayers = mutableListOf<PacketDelayer>()
    val queueSentPackets = Queues.newArrayDeque<Pair<Packet<*>, Long>>()

    fun handleSentPacket(packet: Packet<*>) {
        synchronized(queueSentPackets) {
            queueSentPackets += Pair(packet, System.currentTimeMillis())
        }
    }

    fun handleSentQueue() {
        synchronized(queueSentPackets) {
            queueSentPackets.removeIf {
                if (System.currentTimeMillis() - it.second >= getTotalDelay()) {
                    connection.sendInvisiblePacket(it.first)
                    return@removeIf true
                }

                return@removeIf false
            }
        }
    }

    private fun getTotalDelay() = packetDelayers.sumOf { it.sentPacketsDelayTime }
}