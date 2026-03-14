package haze.module.impl.combat

import haze.event.Event
import haze.event.impl.GameLoopEvent
import haze.event.impl.LegitClickTimingEvent
import haze.event.impl.TickEvent
import haze.module.Category
import haze.module.Module
import haze.module.impl.combat.attackaura.clicking.KillAuraClicker
import haze.module.impl.combat.attackaura.rotating.AttackAuraRotations
import haze.utility.nullCheck
import haze.utility.player.MoveCorrectSetting
import haze.utility.rotation.RotateAble
import haze.utility.target.TargetFinder
import net.minecraft.world.entity.LivingEntity

object AttackAura : Module(
    "AttackAura",
    Category.COMBAT
), RotateAble {
    init {
        registerToRotationHandler()
    }

    var target: LivingEntity? = null

    override fun onDisable() {
        target = null
    }

    private val targetFinder = tree(TargetFinder())
    private val rotations = tree(AttackAuraRotations)
    private val clicker = tree(KillAuraClicker)
    private val moveCorrect = tree(MoveCorrectSetting())

    override val rotateOrdinal = 1

    override fun onEvent(event: Event) {
        if (event is TickEvent.PRE) {
            target = targetFinder.findTarget(target)
        }

        if (target != null) {
            if (event is GameLoopEvent) {
                clicker.calculateClicks()
            }

            if (event is LegitClickTimingEvent) {
                clicker.tryClick(target!!)
            }

            moveCorrect.correctMove(event)
        }
    }

    override fun rotate() {
        target?.let { rotations.rotate(it) }
    }

    override fun shouldRotate() = toggled && nullCheck() && target != null
}