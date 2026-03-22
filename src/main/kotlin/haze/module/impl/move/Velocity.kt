package haze.module.impl.move

import haze.event.Event
import haze.event.impl.AttackEvent
import haze.event.impl.MovementInputEvent
import haze.event.impl.PacketEvent
import haze.module.Category
import haze.module.Module
import haze.utility.player
import net.minecraft.network.protocol.game.ClientboundExplodePacket
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket

object Velocity : Module(
    "Velocity",
    Category.MOVE
) {

    private val modeJump = toggleAbleGroup("Jump", true)
    private val chance by modeJump.number("Chance", 1.0, 0.0..1.0, 0.01)
    private val modeMotion = toggleAbleGroup("Motion", false)
    private val motionXZ by modeMotion.number("MotionXZ", 1.0, -1.0..1.0, 0.1)
    private val motionY by modeMotion.number("MotionY", 1.0, -1.0..1.0, 0.1)
    private val modeReduce = toggleAbleGroup("Reduce", false)
    private val reduceFactor by modeReduce.number("Reduce factor", 1.0, -1.0..1.0, 0.1)
    private val reduceFactorWithSprint by modeReduce.number("Reduce factor with sprint", 1.0, -1.0..1.0, 0.1)

    override fun onEvent(event: Event) {
        when (event) {
            is AttackEvent.PRE -> {
                if (modeReduce.toggled) {
                    if (player.hurtTime != 0) {
                        if (player.isSprinting) {
                            player.isSprinting = false
                            player.deltaMovement.multiply(
                                reduceFactorWithSprint,
                                reduceFactorWithSprint,
                                reduceFactorWithSprint
                            )
                        } else
                            player.deltaMovement.multiply(reduceFactor, reduceFactor, reduceFactor)
                    }
                }
            }

            is MovementInputEvent -> {
                if (modeJump.toggled) {
                    if (player.hurtTime > 8 && player.onGround() && chance < Math.random()) {
                        event.jump = true
                    }
                }
            }

            is PacketEvent.Receive -> {
                if (modeMotion.toggled) {
                    if (event.packet is ClientboundSetEntityMotionPacket && event.packet.id == player.id) {
                        event.cancel()

                        val diff = event.packet.movement.subtract(player.deltaMovement).multiply(
                            motionXZ,
                            motionY,
                            motionXZ
                        )

                        player.deltaMovement = player.deltaMovement.add(diff)
                    }

                    if (event.packet is ClientboundExplodePacket) {
                        event.cancel()

                        val diff = event.packet.playerKnockback.get().subtract(player.deltaMovement).multiply(
                            motionXZ,
                            motionY,
                            motionXZ
                        )

                        player.deltaMovement = player.deltaMovement.add(diff)
                    }
                }
            }
        }
    }
}