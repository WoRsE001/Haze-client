package haze.module.impl.world.scaffold

import haze.utility.player
import kotlin.math.abs

// created by dicves_recode on 28.12.2025
object Lowest : PitchSortMode("Lowest") {
    override fun sort(pitches: MutableList<Float>) {
        pitches.sortWith(Comparator.reverseOrder())
    }
}

object Highest : PitchSortMode("Highest") {
    override fun sort(pitches: MutableList<Float>) {
        pitches.sort()
    }
}

object Mid : PitchSortMode("Mid") {
    override fun sort(pitches: MutableList<Float>) {
        val lowest = pitches.max()
        val highest = pitches.min()
        val mid = (lowest + highest) / 2f

        pitches.sortBy { abs(mid - it) }
    }
}

object Nearest : PitchSortMode("Nearest") {
    override fun sort(pitches: MutableList<Float>) {
        pitches.sortBy { abs(player.xRot - it) }
    }
}
