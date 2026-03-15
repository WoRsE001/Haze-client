package haze.module.impl.combat.attackaura.hitvec

import haze.setting.ConfigureAble
import haze.utility.math.Vec3f
import net.minecraft.world.entity.LivingEntity

// i regret everything, i've done, SCWGxD (05.02.2026:16:17)
abstract class AttackAuraHitVec(name: String) : ConfigureAble(name) {
    val factor by number("Factor", 0.0, 0.0..1.0, 0.01)
    val expandHitboxXZ by number("Expand hitbox by XZ", 1.0, 0.0..2.0, 0.01)
    val expandHitboxY by number("Expand hitbox by Y", 1.0, 0.0..2.0, 0.01)

    abstract fun point(from: LivingEntity, to: LivingEntity): Vec3f
}