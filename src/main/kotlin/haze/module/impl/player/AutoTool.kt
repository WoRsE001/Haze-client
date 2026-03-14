package haze.module.impl.player

import haze.event.Event
import haze.event.impl.LegitClickTimingEvent
import haze.module.Category
import haze.module.Module
import haze.utility.mc
import haze.utility.player
import haze.utility.slot.resetSlot
import haze.utility.level
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.BlockHitResult

// испорченно SCWGxD в 21.12.2025:21:22
object AutoTool : Module(
    "AutoTool",
    Category.PLAYER,
    defaultToggled = true
) {
    var lastSlot = -1

    override fun onEvent(event: Event) {
        if (event is LegitClickTimingEvent) {
            val hitResult = mc.hitResult ?: return
            val targetBlock = if (hitResult is BlockHitResult) hitResult.blockPos else null

            if (targetBlock != null) {
                val bestSlot = findBestTool(targetBlock)

                if (bestSlot != -1 && mc.options.keyAttack.isDown) {
                    player.inventory.selectedSlot = bestSlot
                } else {
                    resetSlot()
                }
            }
        }
    }

    private fun findBestTool(blockPos: BlockPos): Int {
        var bestSpeed = 1f
        var bestSlot = -1

        val blockState = level.getBlockState(blockPos)

        for (i in 0..8) {
            val itemStack = player.inventory.getItem(i)

            val speed = itemStack.getDestroySpeed(blockState)

            if (speed > bestSpeed) {
                bestSpeed = speed
                bestSlot = i
            }
        }

        return bestSlot
    }
}