package haze.module.impl.combat.sprintreset

import haze.event.Event
import haze.event.impl.MovementInputEvent
import haze.event.impl.PacketEvent
import haze.event.impl.SendPosEvent
import haze.event.impl.PlayerStateUpdateEvent
import haze.setting.value.ChoiceValue
import haze.utility.connection
import haze.utility.player
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket

// created by dicves_recode on 29.12.2025
object SprintTap : SprintResetMode("Sprint tap") {
    override fun reset(event: Event): Boolean {
        if (event is PlayerStateUpdateEvent.Post) {
            player.isSprinting = false
            return true
        }

        return false
    }
}

object WTap : SprintResetMode("W tap") {
    override fun reset(event: Event): Boolean {
        if (event is MovementInputEvent) {
            event.forward = false
            return true
        }

        return false
    }
}

object Packet : SprintResetMode("Packet") {
    override fun startReset(event: Event): Boolean {
        if (event is SendPosEvent.Pre) {
            connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING))
            return true
        }

        return false
    }

    override fun reset(event: Event): Boolean {
        if (event is PacketEvent.Send) {
            val packet = event.packet
            if (packet is ServerboundPlayerCommandPacket) {
                if (packet.action == ServerboundPlayerCommandPacket.Action.START_SPRINTING
                    || packet.action == ServerboundPlayerCommandPacket.Action.STOP_SPRINTING) {
                    event.cancel()
                    return true
                }
            }
        }

        return false
    }

    override fun stopReset(event: Event): Boolean {
        if (event is SendPosEvent.Pre) {
            connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_SPRINTING))
            return true
        }

        return false
    }
}

object OnePacket : SprintResetMode("One packet") {
    override fun stopReset(event: Event): Boolean {
        if (event is SendPosEvent.Pre) {
            connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_SPRINTING))
            return true
        }

        return false
    }
}
