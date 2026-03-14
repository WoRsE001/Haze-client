package haze.module.impl.combat.attackaura.hitvec

import haze.setting.ConfigureAble
import haze.utility.math.Vec3f
import net.minecraft.world.entity.LivingEntity

// i regret everything, i've done, SCWGxD (05.02.2026:16:17)
abstract class AttackAuraHitVec(name: String) : ConfigureAble(name) {
    abstract fun point(from: LivingEntity, to: LivingEntity): Vec3f
}