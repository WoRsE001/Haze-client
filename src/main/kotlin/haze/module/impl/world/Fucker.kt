package haze.module.impl.world

import haze.module.Category
import haze.module.Module

// испорченно SCWGxD в 28.12.2025:11:07
object Fucker : Module(
    "Fucker",
    Category.WORLD
) {
    /*private val rotation by boolean("Rotation", true)

    private var targetBlock: BlockPos? = null
    private var isBreaking = false

    override fun onDisable() {
        gameMode.stopDestroyBlock()
        isBreaking = false
    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.PRE) {
            targetBlock = getBestBlock()

            if (rotation) {
                if (targetBlock != null) {
                    CameraRotation.unlocking = true
                    player.xRot = pitchTo(targetBlock!!)
                    player.yRot = yawTo(targetBlock!!)
                } else {
                }
            }

        } else if (event is LegitClickTimingEvent) {
            if (targetBlock != null) {
                if (!isBreaking) {
                    gameMode.startDestroyBlock(targetBlock!!, Direction.UP)
                    isBreaking = true
                } else
                    if (!gameMode.continueDestroyBlock(targetBlock!!, Direction.UP))
                        isBreaking = false

                player.swing(InteractionHand.MAIN_HAND)
            }
        }
    }

    private fun getBestBlock(): BlockPos? {
        val playerPos = player.blockPosition()
        var bestBlock: BlockPos? = null

        for (x in playerPos.x - 5..playerPos.x + 5) {
            for (y in playerPos.y - 4..playerPos.y + 6) {
                for (z in playerPos.z - 5..playerPos.z + 5) {
                    val blockPos = BlockPos(x, y, z)

                    if (level.getBlockState(blockPos).block !is BedBlock)
                        continue

                    if (bestBlock == null ||
                        player.distanceToSqr(x + 0.5, y + 0.5, z + 0.5) <
                        player.distanceToSqr(bestBlock.x + 0.5, bestBlock.y + 0.5, bestBlock.z + 0.5)
                    ) bestBlock = blockPos
                }
            }
        }

        return bestBlock
    }*/
}