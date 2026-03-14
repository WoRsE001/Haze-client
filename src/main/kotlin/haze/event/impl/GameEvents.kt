package haze.event.impl

import haze.event.CancelAbleEvent
import haze.event.Event
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiGraphics

// created by dicves_recode on 28.11.2025
object GameLoopEvent : Event

object LegitClickTimingEvent : Event

interface TickEvent {
    object PRE : CancelAbleEvent()
    object POST : Event
}

interface RenderEvent {
    object Gui : Event {
        lateinit var guiGraphics: GuiGraphics
    }

    object World : Event {
        var poseStack = PoseStack()
        var tickDelta = 0f
    }
}

interface ChatMessageEvent {
    object SEND : CancelAbleEvent() {
        lateinit var content: String
    }
}
