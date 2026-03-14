package haze.module.impl.player

import haze.event.Event
import haze.event.impl.LegitClickTimingEvent
import haze.mixin.accessor.MinecraftAccessor
import haze.module.Category
import haze.module.Module
import haze.module.impl.combat.AttackAura
import haze.utility.mc
import haze.utility.player
import haze.utility.time.Timer
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SplashPotionItem
import kotlin.math.roundToLong

// испорченно SCWGxD в 25.12.2025:19:25
object AutoPotion : Module(
    "AutoPotion",
    Category.PLAYER
) {
    private val prioritizeOverKillAura by boolean("Prioritize over kill aura", true)

    private val potions = group("Potions")

    private val healthPotion by boolean("Health", true, potions)
    private val fireResistantPotion by boolean("Fire resistant", true, potions)
    private val speedPotion by boolean("Speed", true, potions)
    private val strengthPotion by boolean("Strength", true, potions)

    private val thresholdHealth by number("thresholdHealth", 8.0, 1.0..19.0, 0.1).visible { healthPotion }

    private val minCPS by number("Min CPS", 1.0, 0.0..40.0, 0.1)
    private val maxCPS by number("Max CPS", 1.0, 0.0..40.0, 0.1)

    private val timer = Timer()
    private var delay = 0L
    private var lastSlot = -1

    override fun onEvent(event: Event) {
        if (event is LegitClickTimingEvent) {
            if (lastSlot != -1) {
                player.inventory.selectedSlot = lastSlot
                lastSlot = -1
            }

            if (timer.reached < delay)
                return

            if (AttackAura.toggled && AttackAura.target != null && !prioritizeOverKillAura)
                return

            val potionSlot = getPotionSlot(getNeedPotion())

            if (potionSlot == -1)
                return

            if (lastSlot == -1) lastSlot = player.inventory.selectedSlot
            player.inventory.selectedSlot = potionSlot
            (mc as MinecraftAccessor).invokeSartUseItem()

            delay = (1000.0 / minCPS + (maxCPS - minCPS) * Math.random()).roundToLong()
            timer.reset()
        }
    }

    private fun getNeedPotion(): MutableList<Effects> {
        val effects = mutableListOf<Effects>()

        if (healthPotion && player.health <= thresholdHealth)
            effects.add(Effects.Health)
        if (fireResistantPotion && !player.hasEffect(MobEffects.FIRE_RESISTANCE))
            effects.add(Effects.FireResistant)
        if (speedPotion && !player.hasEffect(MobEffects.SPEED))
            effects.add(Effects.Speed)
        if (strengthPotion && !player.hasEffect(MobEffects.STRENGTH))
            effects.add(Effects.Strength)

        return effects
    }

    private fun getPotionSlot(effects: MutableList<Effects>): Int {
        if (effects.isEmpty())
            return -1

        for (i in 0..8) {
            val itemStack = player.inventory.getItem(i)

            if (!itemStack.isEmpty && itemStack.item is SplashPotionItem && isValidPotion(itemStack, effects))
                return i
        }

        return -1
    }

    private fun isValidPotion(itemStack: ItemStack, effects: MutableList<Effects>): Boolean {
        val allEffects = itemStack.get(DataComponents.POTION_CONTENTS)?.allEffects ?: return false

        return allEffects.any { modEffect -> effects.any { modEffect.effect == it.effect } }
    }

    private enum class Effects(val effect: Holder<MobEffect>) {
        FireResistant(MobEffects.FIRE_RESISTANCE),
        Health(MobEffects.INSTANT_HEALTH),
        Speed(MobEffects.SPEED),
        Strength(MobEffects.STRENGTH)
    }
}
