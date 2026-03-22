package haze.module.impl.world.scaffold

import haze.setting.value.ChoiceValue
import haze.utility.player

// created by dicves_recode on 28.12.2025
abstract class PitchSortMode(name: String) : ChoiceValue.Choice(name) {
    fun getPitch(pitches: MutableList<Float>): Float {
        if (pitches.isEmpty())
            return player.xRot

        sort(pitches)
        return pitches[0]
    }

    protected abstract fun sort(pitches: MutableList<Float>)
}
