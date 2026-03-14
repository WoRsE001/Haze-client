package haze.module.impl.move

import haze.event.Event
import haze.event.impl.MovementInputEvent
import haze.event.impl.PacketEvent
import haze.event.impl.SendPosEvent
import haze.event.impl.TickEvent
import haze.event.impl.UpdateEvent
import haze.module.Category
import haze.module.Module
import haze.utility.connection
import haze.utility.player.getYawWithMovement
import haze.utility.player.isMoving
import haze.utility.player
import haze.utility.player.sendInvisiblePacket
import haze.utility.player.withStrafe
import haze.utility.level
import net.minecraft.core.Vec3i
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Input
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

// испорченно SCWGxD в 15:04 27.12.2025
object Speed : Module("Speed", Category.MOVE) {
    private val mode = list("Mode")
    private val modeEntityCollide by mode.subMode("Entity collide")
    private val expandHitbox by number("Expand hitbox", 1.0, 0.0..1.0, 0.1).visible { modeEntityCollide }
    private val collideSpeed by number("Collide speed", 0.08, 0.0..0.2, 0.1).visible { modeEntityCollide }

    private val modeGrim by mode.subMode("Grim")
    private val grimSpeedXZ by number("Speed", 1.0, 0.0..10.0, 0.01).visible { modeGrim }

    private val modeMotion by mode.subMode("Motion").select()
    private val speedXZ by number("Speed", 1.0, 0.0..10.0, 0.1).visible { modeMotion }

    private val modeIntave by mode.subMode("Intave")

    private val autoJump by boolean("Auto jump", true)

    private var sneaking = false

    override fun onDisable() {
        sneaking = false
    }

    override fun onEvent(event: Event) {
        when (event) {
            is MovementInputEvent -> {
                if (autoJump && player.isMoving())
                    event.jump = true
            }

            TickEvent.PRE -> {
                if (modeMotion) {
                    player.deltaMovement = player.deltaMovement.multiply(Vec3(0.0, 1.0, 0.0))
                    player.deltaMovement = player.deltaMovement.withStrafe(speedXZ)
                }
            }

            UpdateEvent.Pre -> {
                if (modeEntityCollide) {
                    if (!player.isMoving())
                        return

                    var collisions = 0
                    val box = player.boundingBox.expandTowards(expandHitbox, expandHitbox, expandHitbox)

                    for (entity in level.entitiesForRendering()) {
                        val entityBox = entity.boundingBox

                        if (entity != player && entity is LivingEntity && entity !is ArmorStand && box.intersects(entityBox)) {
                            collisions++
                        }
                    }

                    val yaw = Math.toRadians(getYawWithMovement(player.yRot, player.input.keyPresses).toDouble())
                    val boost = collideSpeed * collisions
                    player.deltaMovement = player.deltaMovement.add(-sin(yaw) * boost, 0.0, cos(yaw) * boost)
                }  else if (modeIntave) {
                    if (sneaking)
                        player.deltaMovement = player.deltaMovement.multiply(1.043, 1.0, 1.043)
                }
            }

            is PacketEvent.SEND -> {
                val packet = event.packet

                if (modeGrim) {
                    if (packet is ClientboundTeleportEntityPacket) {
                        player.deltaMovement = player.deltaMovement.add(grimSpeedXZ)
                    }
                } else if (modeIntave) {
                    if (packet is ServerboundPlayerInputPacket) {
                        sneaking = false
                        if (!player.isMoving() || player.onGround() || player.hurtTime > 0 || level.isEmptyBlock(player.blockPosition().subtract(Vec3i(0, 1, 0))))
                            return

                        event.cancel()
                        connection.sendInvisiblePacket(ServerboundPlayerInputPacket(Input(packet.input.forward, packet.input.backward, packet.input.left, packet.input.right, packet.input.jump, true, packet.input.sprint)))
                        sneaking = true
                    }
                }
            }

            is SendPosEvent.Pre -> {
                if (modeGrim) {
                    connection.send(ServerboundMovePlayerPacket.Pos(player.position(), player.onGround(), player.horizontalCollision))
                }
            }
        }
    }
}
