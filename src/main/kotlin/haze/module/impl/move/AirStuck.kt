package haze.module.impl.move

import haze.event.Event
import haze.event.impl.MovementEvent
import haze.event.impl.PacketEvent
import haze.event.impl.PlayerStateUpdateEvent
import haze.module.Category
import haze.module.Module
import haze.utility.player
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

// испорченно SCWGxD в 02.01.2026:12:13
object AirStuck : Module(
    "AirStuck",
    Category.MOVE
) {
    private val saveMotion by boolean("Save motion", true)

    var velocity = Vec3(0.0, 0.0, 0.0)

    override fun onEnable() {
        velocity = player.deltaMovement
    }

    override fun onDisable() {
        if (saveMotion)
            player.deltaMovement = velocity
    }

    override fun onEvent(event: Event) {
        if (event is PlayerStateUpdateEvent.Pre) {
            player.deltaMovement = Vec3(0.0, 0.0, 0.0)
        }

        if (event is MovementEvent) {
            event.moveVector = Vec2(0f, 0f)
        }

        if (event is PacketEvent.Send && event.packet is ServerboundMovePlayerPacket) {
            event.cancel()
        }
    }
}