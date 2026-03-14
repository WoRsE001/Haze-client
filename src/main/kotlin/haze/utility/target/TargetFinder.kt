package haze.utility.target

import haze.setting.ConfigureAble
import haze.utility.player
import net.minecraft.world.entity.LivingEntity

// Blood! It's everywhere. SCWxD killed you on 08.03.2026 at 9:24.
class TargetFinder : ConfigureAble("Finding target") {
    private val searchRange by number("Search range", 6.0, 3.0..20.0, 0.1)
    private val sortType = list("Sort type")
    private val sortTypeFOV = sortType.subMode("FOV").select()
    private val sortTypeDistance = sortType.subMode("Distance")
    private val sortTypeHealth = sortType.subMode("Health")
    private val sortTypeHurtTime = sortType.subMode("HurtTime")
    private val lockTarget = toggleAbleGroup("Lock target", false)
    private val lockTargetRange by lockTarget.number("Range", 6.0, 3.0..20.0, 0.1)

    fun findTarget(lastTarget: LivingEntity?): LivingEntity? {
        if (lockTarget.toggled)
            if (lastTarget != null && !lastTarget.isDeadOrDying && player.distanceTo(lastTarget) <= lockTargetRange)
                return lastTarget


        return validTargets(searchRange).bestTargetBy(SortType.valueOf(sortType.get()?.name ?: "FOV"))
    }
}