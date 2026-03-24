package haze.module.impl.combat.attackaura.rotating

import haze.module.impl.combat.attackaura.hitvec.AttackAuraHitVecProcessing
import haze.setting.ToggleableConfigureable
import haze.utility.player
import haze.utility.player.rotation.rotate
import haze.utility.player.rotation.rotation
import haze.utility.player.rotation.rotationTo
import net.minecraft.world.entity.LivingEntity

// created by dicves_recode on 10.01.2026
object AttackAuraRotations : ToggleableConfigureable("Rotations", true) {
    private val hitVecProcessing = tree(AttackAuraHitVecProcessing)
    private val deltaProcessing = tree(AttackAuraDeltaProcessing)

    fun rotate(target: LivingEntity) {
        val point = hitVecProcessing.process(player, target)
        val rotation = rotationTo(point)
        var delta = rotation - player.rotation
        delta = deltaProcessing.process(delta)

        player.rotate(delta)
    }
}
