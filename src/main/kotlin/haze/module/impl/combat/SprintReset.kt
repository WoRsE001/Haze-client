package haze.module.impl.combat

import haze.event.Event
import haze.event.impl.TickEvent
import haze.module.Category
import haze.module.Module
import haze.module.impl.combat.sprintreset.*
import haze.utility.target.lastAttackedTarget

// испорченно SCWGxD в 28.12.2025:20:29
object SprintReset : Module(
    "SprintReset",
    Category.COMBAT
) {
    private val mode = list("Mode")
    private val sprintTap = SprintTap(mode)
    private val wTap = WTap(mode).select()
    private val packet = Packet(mode)
    private val onePacket = OnePacket(mode)
    private val delay by number("Delay", 3.0, 0.0..10.0, 1.0, "ticks")
    private val reset by number("Reset", 2.0, 1.0..10.0, 1.0, "ticks")

    private var delayTimer = 0
    private var resetTimer = 0
    private var isResetting = false

    override fun onDisable() {
        delayTimer = 0
        resetTimer = 0
        isResetting = false
    }

    override fun onEvent(event: Event) {
        val currentSubMode = mode.get()

        if (currentSubMode !is SprintResetMode)
            return

        if (event is TickEvent.PRE) {
            if (lastAttackedTarget != null && lastAttackedTarget?.hurtTime == 10) {
                delayTimer = delay.toInt()
                resetTimer = reset.toInt()
            }

            if (delayTimer > 0) delayTimer--
        }

        if (resetTimer == 0 && isResetting && currentSubMode.stopReset(event))
            isResetting = false

        if (resetTimer == 0 || delayTimer > 0) return

        if (!isResetting && currentSubMode.startReset(event))
            isResetting = true

        if (currentSubMode.reset(event)) {
            resetTimer--
        }
    }
}