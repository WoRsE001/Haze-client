package haze.module.impl.visual

import haze.module.Category
import haze.module.Module
import haze.utility.math.map
import haze.utility.math.roundTo
import haze.utility.mc
import haze.utility.player
import haze.utility.player.rotation.rotation
import haze.utility.sendMessage
import haze.utility.target.SortType
import haze.utility.target.bestTargetBy
import haze.utility.target.validTargets
import kotlin.math.abs

// Blood! It's everywhere. SCWxD killed you on 22.02.2026 at 11:40.
object HitAccuracy : Module("HitAccuracy", Category.VISUAL) {
    fun handleAttack() {
        val target = validTargets(6.0).bestTargetBy(SortType.FOV)

        if (target != null) {
            val deltaAccuracy = (player.rotation - player.rotation.coerceIn(target.boundingBox)).fixed()
            val accuracy = map(abs(deltaAccuracy.yaw.toDouble()), 0.0, 180.0, 100.0, 0.0).roundTo(0.01)
            mc.sendMessage("Hit: $accuracy%")
        }
    }
}