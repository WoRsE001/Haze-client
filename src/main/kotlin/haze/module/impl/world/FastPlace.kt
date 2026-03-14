package haze.module.impl.world

import haze.event.Event
import haze.event.impl.GameLoopEvent
import haze.module.Category
import haze.module.Module
import haze.utility.math.randomDouble
import haze.utility.mc
import haze.utility.player
import haze.utility.player.Clicker
import haze.utility.time.Timer
import net.minecraft.world.item.BlockItem
import kotlin.math.roundToLong

// испорченно SCWGxD в 28.12.2025:11:22
object FastPlace : Module(
    "FastPlace",
    Category.WORLD
) {
    private val minCPS by number("Min CPS", 17.0, 0.0..40.0, 0.1)
    private val maxCPS by number("Max CPS", 20.0, 0.0..40.0, 0.1)

    private val onlyWhileBlocksInHand by boolean("Only while blocks in hand", true)

    private val timer = Timer()
    private var delay = 0L

    override fun onEvent(event: Event) {
        if (event is GameLoopEvent
            && !player.isUsingItem
            && (player.inventory.selectedItem.item is BlockItem || !onlyWhileBlocksInHand)
            && mc.options.keyUse.isDown
            && timer.reached >= delay
            ) {
            delay = (1000.0 / randomDouble(minCPS, maxCPS)).roundToLong()
            Clicker.rightClick()
            timer.reset()
        }
    }
}