package haze.module.impl.combat

import haze.event.Event
import haze.event.impl.GameLoopEvent
import haze.module.Category
import haze.module.Module
import haze.utility.mc
import haze.utility.player
import haze.utility.player.Clicker
import haze.utility.time.Timer
import net.minecraft.world.phys.HitResult
import kotlin.math.roundToLong

// испорченно SCWGxD в 14.12.2025:17:53
object AutoClicker : Module(
    "AutoClicker",
    Category.COMBAT,
    defaultToggled = true
) {
    private val minCPS by number("Min CPS", 17.0, 0.0..40.0, 0.1)
    private val maxCPS by number("Max CPS", 20.0, 0.0..40.0, 0.1)

    private val timer = Timer()
    private var delay = 0L

    override fun onEvent(event: Event) {
        if (event is GameLoopEvent && !player.isUsingItem && mc.options.keyAttack.isDown) {
            if (mc.hitResult?.type == HitResult.Type.BLOCK)
                return

            if (timer.reached >= delay) {
                delay = (1000.0 / minCPS + (maxCPS - minCPS) * Math.random()).roundToLong()
                Clicker.leftClick()
                timer.reset()
            }
        }
    }
}