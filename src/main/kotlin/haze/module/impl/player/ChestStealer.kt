package haze.module.impl.player

import haze.event.Event
import haze.event.impl.TickEvent
import haze.module.Category
import haze.module.Module
import haze.utility.gameMode
import haze.utility.player
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.ClickType

// испорченно SCWGxD в 17.12.2025:16:42
object ChestStealer : Module(
   "ChestStealer",
    Category.PLAYER,
) {
    private val fixSwap by boolean("Fix swap", true)

    override fun onEvent(event: Event) {
        if (event is TickEvent.PRE) {
            if (player.containerMenu is ChestMenu) {
                for (i in 0..<(player.containerMenu as ChestMenu).container.containerSize) {
                    if (!player.containerMenu.getSlot(i).item.isEmpty) {
                        gameMode.handleInventoryMouseClick(
                            player.containerMenu.containerId,
                            i,
                            0,
                            if (fixSwap) ClickType.PICKUP else ClickType.QUICK_MOVE,
                            player
                        )
                        if (fixSwap) {
                            for (j in (player.containerMenu as ChestMenu).container.containerSize..<player.containerMenu.slots.size) {
                                if (player.containerMenu.getSlot(j).item.isEmpty) {
                                    gameMode.handleInventoryMouseClick(
                                        player.containerMenu.containerId,
                                        j,
                                        0,
                                        ClickType.PICKUP,
                                        player
                                    )
                                    break
                                }
                            }
                        }
                        break
                    }
                }
            }
        }
    }
}