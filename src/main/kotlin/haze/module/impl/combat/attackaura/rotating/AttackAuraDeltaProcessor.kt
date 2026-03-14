package haze.module.impl.combat.attackaura.rotating

import haze.setting.ToggleAbleConfigureAble
import haze.utility.rotation.Rotation

// created by dicves_recode on 10.01.2026
abstract class AttackAuraDeltaProcessor(name: String) : ToggleAbleConfigureAble(name, false) {
    abstract fun process(delta: Rotation): Rotation
}
