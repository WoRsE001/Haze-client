package haze.module.impl.combat.attackaura.rotating

import haze.setting.Configureable
import haze.utility.player.rotation.Rotation

// created by dicves_recode on 10.01.2026
object AttackAuraDeltaProcessing : Configureable("Delta processing") {
    val clamp = tree(ClampDeltaProcessor)
    val basicRandomize = tree(BasicRandomizeDeltaProcessor)
    val linearSmooth = tree(LinearSmoothDeltaProcessor)
    val mixDelta = tree(MixDeltaDeltaProcessor)
    val gcdFix = tree(GcdFixDeltaProcessor)

    private val processors = mutableListOf(clamp, basicRandomize, linearSmooth, mixDelta, gcdFix)

    fun process(delta: Rotation): Rotation {
        var delta = delta

        for (processor in processors) {
            if (processor.toggled)
                delta = processor.process(delta)
        }

        return delta
    }
}
