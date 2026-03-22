package haze.utility.player

import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Player

// испорченно SCWGxD в 20.12.2025:21:28
var utilAirTicks = 0
var utilGroundTicks = 0

val LocalPlayer.airTicks: Int
    get() = utilAirTicks

val LocalPlayer.groundTicks: Int
    get() = utilGroundTicks

fun Player.canCrit() = fallDistance > 0.0F && !onGround() && !onClimbable() && !isInWater && !isMobilityRestricted && !isPassenger
