package haze.utility.player

import net.minecraft.world.inventory.InventoryMenu

// Blood! It's everywhere. SCWxD killed you on 17.03.2026 at 14:39.
fun InventoryMenu.isFull() = slots.all { !it.item.isEmpty }