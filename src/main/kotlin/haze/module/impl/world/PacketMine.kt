package haze.module.impl.world

import haze.event.Event
import haze.event.impl.TickEvent
import haze.module.Category
import haze.module.Module
import haze.utility.connection
import haze.utility.mc
import haze.utility.level
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
import net.minecraft.world.phys.BlockHitResult

// created by dicves_recode on 05.12.2025
object PacketMine : Module(
    "PacketMine",
    Category.WORLD
) {
    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            val crosshairTarget = mc.hitResult ?: return

            if (crosshairTarget !is BlockHitResult)
                return

            val pos = crosshairTarget.blockPos

            if (level.getBlockState(pos).isAir)
                return

            if (mc.options.keyPickItem.isDown)
                mine(pos, crosshairTarget.direction)
        }
    }

    fun mine(pos: BlockPos, direction: Direction) {
        connection.send(
            ServerboundPlayerActionPacket(
                ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,
                pos,
                direction
            )
        )
        connection.send(
            ServerboundPlayerActionPacket(
                ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,
                pos,
                direction
            )
        )
    }
}