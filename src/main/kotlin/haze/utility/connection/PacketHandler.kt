package haze.utility.connection

import com.google.common.collect.Queues
import net.minecraft.network.protocol.Packet

// Blood! It's everywhere. SCWxD killed you on 14.02.2026 at 4:18.
object PacketHandler {
    val packetQueue = Queues.newArrayDeque<Pair<Packet<*>, Long>>()
}