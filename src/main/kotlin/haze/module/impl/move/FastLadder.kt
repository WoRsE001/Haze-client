package haze.module.impl.move

import haze.event.Event
import haze.event.impl.PlayerStateUpdateEvent
import haze.module.Category
import haze.module.Module
import haze.utility.player
import net.minecraft.world.phys.Vec3

// испорченно SCWGxD в 02.01.2026:13:29
object FastLadder : Module(
    "FastLadder",
    Category.MOVE
) {
    private val mode = list("Mode")
    private val modeMotion = mode.choice("Motion").select()

    private val motion by number("Motion", 1.0, 0.0..2.0, 0.1).visible { modeMotion.selected() }

    private val spoofGround by boolean("Spoof ground", false)

    override fun onEvent(event: Event) {
        if (event is PlayerStateUpdateEvent.Pre && player.horizontalCollision && player.onClimbable()) {
            when (mode.get()) {
                modeMotion -> {
                    player.deltaMovement = Vec3(player.deltaMovement.x, motion, player.deltaMovement.z)
                }
            }

            player.setOnGround(player.onGround() || spoofGround)
        }
    }
}