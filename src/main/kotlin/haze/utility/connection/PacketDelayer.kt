package haze.utility.connection

// Blood! It's everywhere. SCWxD killed you on 19.03.2026 at 1:58.
interface PacketDelayer {
    var sentPacketsDelayTime: Int
    var holdSentPackets: Boolean

    fun registerToPacketHandler() { PacketHandler.packetDelayers.add(this) }
    fun resetSendDelay() {}
}