package haze.utility.render

import haze.utility.math.Rect
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.renderer.RenderPipelines
import org.joml.Matrix3x2f

// Blood! It's everywhere. SCWxD killed you on 03.03.2026 at 12:44.
object Render2D {
    lateinit var guiGraphics: GuiGraphics

    fun drawRect(rect: Rect, color: IntArray) {
        guiGraphics.guiRenderState.submitGuiElement(RectRenderState(
            RenderPipelines.GUI,
            TextureSetup.noTexture(),
            Matrix3x2f(guiGraphics.pose()),
            rect,
            color,
            guiGraphics.scissorStack.peek()
        ))
    }

    fun drawRect(rect: Rect, color: Int) {
        drawRect(rect, intArrayOf(color, color, color, color))
    }
}