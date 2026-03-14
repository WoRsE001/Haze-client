package haze.module.impl.combat

import haze.event.Event
import haze.event.impl.CursorDeltaEvent
import haze.event.impl.TickEvent
import haze.module.Category
import haze.module.Module
import haze.utility.player
import haze.utility.rotation.rotation
import haze.utility.rotation.rotationTo
import haze.utility.rotation.scale
import haze.utility.target.TargetFinder
import net.minecraft.world.entity.LivingEntity
import kotlin.math.roundToLong
import kotlin.math.sign

// испорченно SCWGxD в 17.12.2025:7:43
object AimAssist : Module(
    "AimAssist",
    Category.COMBAT
) {
    private val targetFinder = tree(TargetFinder())

    private val yawSpeed by number("Yaw speed", 2.0, 1.0..10.0, 0.1)
    private val moveVertical by boolean("Move vertical", false)
    private val pitchSpeed by number("Pitch speed", 2.0, 1.0..10.0, 0.1).visible { moveVertical }

    var target: LivingEntity? = null

    override fun onEvent(event: Event) {
        if (event is TickEvent.PRE) {
            AttackAura.target = targetFinder.findTarget(AttackAura.target)
        }

        if (event is CursorDeltaEvent && target != null) {
            val point = player.rotation.coerceIn(target!!.boundingBox.scale(0.9, 0.9)).asVec().toVec3f()
            val deltaToTarget = (rotationTo(point) - player.rotation).wrapped()

            event.accumulatedDX = if (deltaToTarget.yaw.sign.toDouble() == event.accumulatedDX.sign)
                     (event.accumulatedDX * yawSpeed).roundToLong().toDouble()
                else (event.accumulatedDX / yawSpeed).roundToLong().toDouble()

            if (moveVertical) {
                event.accumulatedDY = if (deltaToTarget.pitch.sign.toDouble() == event.accumulatedDY.sign)
                    (event.accumulatedDY * pitchSpeed).roundToLong().toDouble()
                else (event.accumulatedDY / pitchSpeed).roundToLong().toDouble()
            }
        }
    }
}