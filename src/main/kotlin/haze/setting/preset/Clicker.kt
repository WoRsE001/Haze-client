package haze.setting.preset

import haze.event.Event
import haze.event.impl.GameLoopEvent
import haze.event.impl.LegitClickTimingEvent
import haze.setting.ConfigureAble
import haze.utility.gameMode
import haze.utility.math.random
import haze.utility.player
import haze.utility.player.canCrit
import haze.utility.player.facingEnemy
import haze.utility.time.Timer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity

// Blood! It's everywhere. SCWxD killed you on 22.03.2026 at 6:35.
class Clicker : ConfigureAble("Clicker") {
    private val clickType = list("Attack type")
    private val clickTypeLegacy by clickType.subMode("Legacy").select()
    private val clickTypeModern by clickType.subMode("Modern")
    private val critType = list("Crit type")
    private val critTypeNone by critType.subMode("None").select()
    private val critTypeSmart by critType.subMode("Smart")
    private val critTypeAlways by critType.subMode("Always")
    private val CPS by numberRange("CPS", 20.0..20.0, 0.0..40.0, 0.1).visible { clickTypeLegacy }
    private val attackRange by number("Attack range", 3.0, 0.0..6.0, 0.1)
    private val clickRange by number("Click range", 8.0, 0.0..20.0, 0.1)

    private val timer = Timer()
    private var delay = 0f
    var clicks = 0

    fun calculateClicks() {
        if (timer.reached >= delay && shouldAttack()) {
            delay = 1000f / if (clickTypeModern) 5f else CPS.random().toFloat()
            clicks++
            timer.reset()
        }
    }

    fun tryClick(target: LivingEntity) {
        repeat(clicks) {
            if (facingEnemy(player, target, attackRange, 0.0)) {
                gameMode.attack(player, target)
                player.swing(InteractionHand.MAIN_HAND)
            } else if (player.distanceTo(target) <= clickRange) {
                player.resetAttackStrengthTicker()
                player.swing(InteractionHand.MAIN_HAND)
            }
        }

        clicks = 0
    }

    fun processClicks(event: Event, target: LivingEntity) {
        if (event is GameLoopEvent)
            calculateClicks()
        else if (event is LegitClickTimingEvent)
            tryClick(target)
    }

    fun shouldAttack(): Boolean {
        if (player.getAttackStrengthScale(0.5f) > 0.9 || clickTypeLegacy) {
            if (player.onGround() && critTypeSmart || player.canCrit() || critTypeNone)
                return true
        }

        return false
    }
}