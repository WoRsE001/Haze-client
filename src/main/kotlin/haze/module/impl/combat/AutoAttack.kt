package haze.module.impl.combat

import haze.event.Event
import haze.module.Category
import haze.module.Module
import haze.setting.preset.Clicker
import haze.setting.preset.TargetFinder
import net.minecraft.world.entity.LivingEntity

// Blood! It's everywhere. SCWxD killed you on 22.03.2026 at 6:21.
object AutoAttack : Module("AutoAttack", Category.COMBAT) {
    private val targetFinder = tree(TargetFinder())
    private val clicker = tree(Clicker())

    private var target: LivingEntity? = null

    override fun onEvent(event: Event) {
        target = targetFinder.findTarget(target)
        clicker.processClicks(event, target ?: return)
    }
}