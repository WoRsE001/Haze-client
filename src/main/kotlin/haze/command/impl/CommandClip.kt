package haze.command.impl

import haze.command.Command
import haze.utility.connection
import haze.utility.mc
import haze.utility.player
import haze.utility.player.rotation.CameraRotation
import haze.utility.sendMessage
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

// Blood! It's everywhere. SCWxD killed you on 09.03.2026 at 11:23.
object CommandClip : Command(
    "Clip",
    "Clip through wall",
    ".clip xz y",
    "clip"
) {
    override fun execute(args: List<String>) {
        if (args.size != 2) {
            mc.sendMessage(usage)
            return
        }

        val x = args[0].toFloat() * -sin(Math.toRadians(CameraRotation.yaw.toDouble()))
        val z = args[0].toFloat() * cos(Math.toRadians(CameraRotation.yaw.toDouble()))

        val lastPos = player.position()
        val pos = Vec3(player.x + x, player.y + args[1].toDouble(), player.z + z)
        connection.send(ServerboundMovePlayerPacket.Pos(pos, player.onGround(), player.horizontalCollision))
        connection.send(ServerboundMovePlayerPacket.Pos(lastPos, player.onGround(), player.horizontalCollision))
    }
}