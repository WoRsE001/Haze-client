package haze.module.impl.combat.attackaura.hitvec

import haze.utility.math.Vec3f
import haze.utility.rotation.CameraRotation
import haze.utility.rotation.rotation
import haze.utility.rotation.expand
import net.minecraft.world.entity.LivingEntity

// i regret everything, i've done, SCWGxD (05.02.2026:16:25)
object BodyPartHitVec : AttackAuraHitVec("Head") {
    override fun point(from: LivingEntity, to: LivingEntity): Vec3f {
        return Vec3f(to.eyePosition)
    }
}

object BestHitVec : AttackAuraHitVec("Best") {
    override fun point(from: LivingEntity, to: LivingEntity): Vec3f {
        return Vec3f(from.eyePosition).coerceIn(to.boundingBox.expand(expandHitboxXZ, expandHitboxY))
    }
}

object NearestHitVec : AttackAuraHitVec("Nearest") {
    override fun point(from: LivingEntity, to: LivingEntity): Vec3f {
        return CameraRotation.coerceIn(to.boundingBox.expand(expandHitboxXZ, expandHitboxY)).toVec3f() + Vec3f(from.eyePosition)
    }
}