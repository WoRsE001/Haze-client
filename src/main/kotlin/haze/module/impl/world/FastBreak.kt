package haze.module.impl.world

import haze.event.Event
import haze.event.impl.TickEvent
import haze.mixin.accessor.MinecraftAccessor
import haze.mixin.accessor.MultiPlayerGameModeAccessor
import haze.module.Category
import haze.module.Module
import haze.utility.gameMode
import haze.utility.mc

// created by dicves_recode on 05.12.2025
object FastBreak : Module(
    "FastBreak",
    Category.WORLD,
) {
    private val destroyOnBreakProgress by number("Destroy on break progress", 0.5, 0.0..1.0, 0.01)
    private val noBlockHitDelay by boolean("No block hit delay", false)
    private val legitNoBlockHitDelay by boolean("Legit", false).visible { noBlockHitDelay }
    private val numberOfClicks by number("Number of clicks",
        1.0,
        1.0..10.0,
        1.0).visible { noBlockHitDelay && legitNoBlockHitDelay }
    override fun onEvent(event: Event) {
        if (event is TickEvent.PRE) {
            val accessor = gameMode as MultiPlayerGameModeAccessor

            if (accessor.destroyProgress >= destroyOnBreakProgress)
                accessor.destroyProgress = 1f

            if (noBlockHitDelay && accessor.destroyDelay > 0) {
                if (legitNoBlockHitDelay) {
                    repeat(numberOfClicks.toInt()) {
                        (mc as MinecraftAccessor).invokeStartAttack()
                    }
                }

                accessor.setDestroyDelay(0)
            }
        }
    }
}