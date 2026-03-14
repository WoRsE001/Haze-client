package haze.utility.slot

import haze.utility.player

// created by dicves_recode on 28.12.2025
var fakeSelected: Int = 0

fun resetSlot() {
    player.inventory.selectedSlot = fakeSelected
}
