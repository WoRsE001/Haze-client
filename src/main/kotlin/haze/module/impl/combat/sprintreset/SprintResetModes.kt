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
class SprintTap(parent: ChoiceValue) : SprintResetMode("Sprint tap", parent) {
    override fun reset(event: Event): Boolean {
        if (event is PlayerStateUpdateEvent.Post) {
            player.isSprinting = false
            return true
        }

        return false
    }
}

class WTap(parent: ChoiceValue) : SprintResetMode("W tap", parent) {
    override fun reset(event: Event): Boolean {
        if (event is MovementInputEvent) {
            event.forward = false
            return true
        }

        return false
    }
}

class Packet(parent: ChoiceValue) : SprintResetMode("Packet", parent) {
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

class OnePacket(parent: ChoiceValue) : SprintResetMode("One packet", parent) {
    override fun stopReset(event: Event): Boolean {
        if (event is SendPosEvent.Pre) {
            connection.send(ServerboundPlayerCommandPacket(player, ServerboundPlayerCommandPacket.Action.START_SPRINTING))
            return true
        }

        return false
    }
}
