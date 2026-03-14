package haze.gui

import haze.utility.math.Rect
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.MouseButtonEvent

// created by dicves_recode on 21.12.2025
interface RenderAble {
    var rect: Rect

    fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float)
    fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean) = false
    fun mouseReleased(mouseButtonEvent: MouseButtonEvent) = false
    fun mouseScrolled(mouseX: Double, mouseY: Double, hScroll: Double, vScroll: Double) = false
}
