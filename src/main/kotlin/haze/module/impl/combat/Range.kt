package haze.module.impl.combat

import haze.module.Category
import haze.module.Module
import haze.utility.player

// испорченно SCWGxD в 23.12.2025:18:48
object Range : Module(
    "Reach",
    Category.COMBAT
) {
    val attackReach by number("Attack reach", 3.0, 0.0..6.0, 0.01)
    val blockReach by number("Block reach", 4.5, 0.0..6.0, 0.01)

    override fun onEnable() {
        player.entityInteractionRange()
    }
}