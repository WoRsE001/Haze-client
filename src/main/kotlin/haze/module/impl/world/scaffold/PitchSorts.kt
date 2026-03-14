package haze.module.impl.world.scaffold

import haze.setting.value.ChoiceValue
import haze.utility.player
import kotlin.math.abs

// created by dicves_recode on 28.12.2025
class Lowest(parent: ChoiceValue) : PitchSortMode("Lowest", parent) {
    override fun sort(pitches: MutableList<Float>) {
        pitches.sortWith(Comparator.reverseOrder())
    }
}

class Highest(parent: ChoiceValue) : PitchSortMode("Highest", parent) {
    override fun sort(pitches: MutableList<Float>) {
        pitches.sort()
    }
}

class Mid(parent: ChoiceValue) : PitchSortMode("Mid", parent) {
    override fun sort(pitches: MutableList<Float>) {
        val lowest = pitches.max()
        val highest = pitches.min()
        val mid = (lowest + highest) / 2f

        pitches.sortBy { abs(mid - it) }
    }
}

class Nearest(parent: ChoiceValue) : PitchSortMode("Nearest", parent) {
    override fun sort(pitches: MutableList<Float>) {
        pitches.sortBy { abs(player.xRot - it) }
    }
}
