package haze.utility.player

import haze.event.Event
import haze.event.EventListener
import haze.event.impl.LegitClickTimingEvent
import haze.mixin.accessor.MinecraftAccessor
import haze.utility.mc

// испорченно SCWGxD в 14.12.2025:18:15
object Clicker : EventListener {
    init {
        registerToEvents()
    }

    private var leftClicks = 0
    private var rightClicks = 0

    fun leftClick() {
        leftClicks++
    }

    fun rightClick() {
        rightClicks++
    }

    override fun onEvent(event: Event) {
        if (event is LegitClickTimingEvent) {
            repeat(leftClicks) {
                (mc as MinecraftAccessor).invokeStartAttack()
            }

            repeat(rightClicks) {
                (mc as MinecraftAccessor).invokeSartUseItem()
            }

            leftClicks = 0
            rightClicks = 0
        }
    }

    override fun shouldListenEvents() = mc.screen == null
}