package haze.module.impl.move

import haze.event.Event
import haze.event.impl.PacketEvent
import haze.event.impl.SlowDownEvent
import haze.event.impl.UpdateEvent
import haze.module.Category
import haze.module.Module
import haze.utility.connection
import haze.utility.player
import haze.utility.player.isFood
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.phys.BlockHitResult

// испорченно SCWGxD в 30.12.2025:8:28
object NoSlow : Module(
    "NoSlow",
    Category.MOVE
) {
    private val consume = toggleAbleGroup("Consume", true)
    private val consumeMode = consume.list("Mode")
    private val consumeModeNone by consumeMode.subMode("None").select()
    private val consumeModeIntave by consumeMode.subMode("Intave")
    private val consumeModeGrim by consumeMode.subMode("Grim")
    private val consumeModeGrimTicks by consume.number("Ticks", 2.0, 1.0..5.0, 1.0).visible { consumeModeGrim }
    private val consumeSlowFactor by consume.number("Slow factor", 1.0, 0.0..1.0, 0.1)
    private val consumeSprint by consume.boolean("Sprint", true)

    private val sword = toggleAbleGroup("Sword", true)
    private val swordMode = consume.list("Mode")
    private val swordModeNone by swordMode.subMode("None").select()
    private val swordModeIntave by swordMode.subMode("Intave")
    private val swordModeGrim by swordMode.subMode("Grim")
    private val swordModeGrimTicks by consume.number("Ticks", 2.0, 1.0..5.0, 1.0).visible { swordModeGrim }
    private val swordSlowFactor by consume.number("Slow factor", 1.0, 0.0..1.0, 0.1)
    private val swordSprint by consume.boolean("Sprint", true)

    override fun onEvent(event: Event) {
        if (event is SlowDownEvent) {
            if (consume.toggled && player.useItem.isFood) {
                event.slowDown = consumeSlowFactor.toDouble()
                event.sprint = consumeSprint

                if (consumeModeGrim && player.tickCount % consumeModeGrimTicks.toInt() != 0) {
                    event.slowDown = 0.2
                    event.sprint = false
                }
            }

            if (sword.toggled && player.useItem.useAnimation == ItemUseAnimation.BLOCK) {
                event.slowDown = swordSlowFactor.toDouble()
                event.sprint = swordSprint

                if (swordModeGrim && player.tickCount % swordModeGrimTicks.toInt() != 0) {
                    event.slowDown = 0.2
                    event.sprint = false
                }
            }
        }

        if (event is UpdateEvent.Pre) {
            if (consume.toggled && player.useItem.isFood && consumeModeIntave) {
                connection.send(
                    ServerboundPlayerActionPacket(
                        ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM,
                        player.blockPosition(),
                        Direction.UP
                    )
                )
            }
        }

        if (event is PacketEvent.SEND) {
            val packet = event.packet

            if (packet is ServerboundUseItemOnPacket) {
                if (sword.toggled && player.useItem.useAnimation == ItemUseAnimation.BLOCK && swordModeIntave) {
                    connection.send(
                        ServerboundUseItemOnPacket(
                            packet.hand, BlockHitResult(
                                packet.hitResult.blockPos.center,
                                packet.hitResult.direction,
                                packet.hitResult.blockPos,
                                packet.hitResult.isInside
                            ), packet.sequence
                        )
                    )
                }
            }
        }
    }
}