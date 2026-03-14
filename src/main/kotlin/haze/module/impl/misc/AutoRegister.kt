package haze.module.impl.misc

import haze.event.Event
import haze.event.impl.PacketEvent
import haze.module.Category
import haze.module.Module
import haze.utility.connection
import haze.utility.time.Timer
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket

// испорченно SCWGxD в 16.12.2025:17:07
object AutoRegister : Module(
    "AutoRegister",
    Category.MISC
) {
    private val delay by number("Delay", 200.0, 0.0..10000.0, 1.0)

    private const val PASSWORD = "cheter777"
    private val timer = Timer()

    override fun onEvent(event: Event) {
        if (event is PacketEvent.RECEIVE && event.packet is ClientboundSystemChatPacket && timer.reached >= delay) {
            val content = event.packet.content.toString()

            if ("/reg" in content) {
                connection.sendCommand("register $PASSWORD $PASSWORD")
                timer.reset()
            } else if ("/login" in content) {
                connection.sendCommand("login $PASSWORD")
                timer.reset()
            }
        }
    }
}