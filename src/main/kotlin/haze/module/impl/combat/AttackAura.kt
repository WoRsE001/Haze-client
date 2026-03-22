package haze.module.impl.combat

import haze.event.Event
import haze.event.impl.TickEvent
import haze.module.Category
import haze.module.Module
import haze.module.impl.combat.attackaura.rotating.AttackAuraRotations
import haze.setting.preset.Clicker
import haze.utility.nullCheck
import haze.setting.preset.MoveCorrector
import haze.utility.player.rotation.RotateAble
import haze.setting.preset.TargetFinder
import net.minecraft.world.entity.LivingEntity

object AttackAura : Module(
    "AttackAura",
    Category.COMBAT
), RotateAble {
    override val rotateOrdinal = 1

    private val targetFinder = tree(TargetFinder())
    private val rotations = tree(AttackAuraRotations)
    private val clicker = tree(Clicker())
    private val moveCorrect = tree(MoveCorrector())

    var target: LivingEntity? = null

    init {
        registerToRotationHandler()
    }

    override fun onDisable() {
        target = null
    }

    override fun onEvent(event: Event) {
        if (event is TickEvent.Pre) {
            target = targetFinder.findTarget(target)
        }

        target?.let {
            clicker.processClicks(event, target!!)
            moveCorrect.correctMove(event)
        }
    }

    override fun rotate() {
        target?.let { rotations.rotate(it) }
    }

    override fun shouldRotate() = toggled && nullCheck() && target != null
}