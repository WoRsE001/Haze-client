package haze.module.impl.combat.attackaura.hitvec

import haze.setting.Configureable
import haze.utility.math.Vec3f
import net.minecraft.world.entity.LivingEntity

// Blood! It's everywhere. SCWxD killed you on 12.03.2026 at 12:39.
object AttackAuraHitVecProcessing : Configureable("Hit vector") {
    private val bodyPartHitVec = tree(BodyPartHitVec)
    private val bestHitVec = tree(BestHitVec)
    private val nearestHitVec = tree(NearestHitVec)

    private val hitVecs = mutableListOf(bodyPartHitVec, bestHitVec, nearestHitVec)

    fun process(from: LivingEntity, to: LivingEntity): Vec3f {
        val point = Vec3f(0f, 0f, 0f)
        val sum = hitVecs.sumOf { it.factor }

        for (hitVec in hitVecs) {
            point += hitVec.point(from, to) * hitVec.factor.toFloat()
        }

        point /= sum.toFloat()

        return point
    }
}