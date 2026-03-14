package haze.module.impl.combat

import haze.event.Event
import haze.event.impl.AttackEvent
import haze.module.Category
import haze.module.Module
import haze.utility.connection
import haze.utility.player
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

// испорченно SCWGxD в 21.12.2025:22:15
object Criticals : Module(
    "Criticals",
    Category.COMBAT
){
    override fun onEvent(event: Event) {
        if (event is AttackEvent.PRE) {
            connection.send(ServerboundMovePlayerPacket.Pos(player.position().add(0.0, 1.1E-5, 0.0), false, false))
            connection.send(ServerboundMovePlayerPacket.Pos(player.position(), false, false))
        }
    }
}