package haze.module.impl.move

import haze.event.Event
import haze.event.impl.PacketEvent
import haze.event.impl.UpdateEvent
import haze.module.Category
import haze.module.Module
import haze.utility.player
import haze.utility.player.withStrafe
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.world.phys.Vec3

// i regret everything, i've done, SCWGxD (06.02.2026:15:48)
object LongJump : Module(
    "LongJump",
    Category.MOVE
) {
    private val boostSpeed by number("Boost Speed", 2.1, -3.0..8.0, 0.01)

    private var receivedFlag = false
    private var canBoost = false
    private var boosted = false
    private var touchGround = false

    override fun onEnable() {
        boosted = false
        canBoost = false
        receivedFlag = false
        touchGround = false
    }

    override fun onEvent(event: Event) {
        when (event) {
            is PacketEvent.RECEIVE -> {
                val packet = event.packet
                if (packet is ClientboundPlayerPositionPacket) {
                    receivedFlag = true
                }
            }

            is UpdateEvent.Pre -> {
                if (!player.onGround() && touchGround) {
                    touchGround = false
                }

                if (player.onGround() && !touchGround) {
                    player.jumpFromGround()
                    boosted = false
                }

                if (player.fallDistance >= 0.25 && !boosted) {
                    canBoost = true
                }

                if (canBoost) {
                    player.deltaMovement = player.deltaMovement.withStrafe(boostSpeed)
                    player.deltaMovement = Vec3(player.deltaMovement.x, 0.42, player.deltaMovement.z)

                    boosted = true
                }

                if (receivedFlag && boosted) {
                    receivedFlag = false
                    canBoost = false
                    toggle()
                }
            }
        }
    }
}