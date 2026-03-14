package haze.module.impl.player

import haze.event.Event
import haze.event.impl.UpdateEvent
import haze.module.Category
import haze.module.Module
import haze.utility.connection
import haze.utility.math.roundTo
import haze.utility.player
import haze.utility.rotation.gcd
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket

// испорченно SCWGxD в 09.01.2026:23:16
object FastUse : Module(
    "FastUse",
    Category.PLAYER
) {
    override fun onEvent(event: Event) {
        if (event is UpdateEvent.Pre) {
            connection.send(ServerboundMovePlayerPacket.PosRot(player.position(), player.yRot, player.xRot + 1.0.roundTo(gcd()).toFloat(), player.onGround(), player.horizontalCollision))
        }
    }
}