package haze.utility.render

import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.Std140Builder
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.systems.RenderSystem
import haze.utility.math.Rect
import haze.utility.mc
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.renderer.MappableRingBuffer

// Blood! It's everywhere. SCWxD killed you on 03.03.2026 at 12:44.
object Render2D {
    private val ubo_map = linkedMapOf<String, MappableRingBuffer>()

    fun drawGradientRect(guiGraphics: GuiGraphics, rect: Rect, color: IntArray) {
        val state = SimpleGuiElement(
            guiGraphics,
            CustomRenderPipelines.RECT,
            TextureSetup.noTexture(),
            rect.lt.x, rect.lt.y, rect.size.x, rect.size.y
        ) { vertexConsumer ->
            vertexConsumer.addVertexWith2DPose(guiGraphics.pose(), rect.lt.x, rect.lt.y).setColor(color[0])
            vertexConsumer.addVertexWith2DPose(guiGraphics.pose(), rect.lb.x, rect.lb.y).setColor(color[1])
            vertexConsumer.addVertexWith2DPose(guiGraphics.pose(), rect.rb.x, rect.rb.y).setColor(color[2])
            vertexConsumer.addVertexWith2DPose(guiGraphics.pose(), rect.rt.x, rect.rt.y).setColor(color[3])
        }

        guiGraphics.guiRenderState.submitGuiElement(state)
    }

    fun drawRect(guiGraphics: GuiGraphics, rect: Rect, color: Int) {
        drawGradientRect(guiGraphics, rect, intArrayOf(color, color, color, color))
    }

    fun drawText(guiGraphics: GuiGraphics, text: String, x: Float, y: Float, color: Int) {
        guiGraphics.drawString(mc.font, text, x.toInt(), y.toInt(), color, false)
    }

    fun drawCentredText(guiGraphics: GuiGraphics, text: String, x: Float, y: Float, color: Int) {
        guiGraphics.drawString(mc.font, text, (x - mc.font.width(text) / 2f).toInt(), (y - mc.font.lineHeight / 2f).toInt(), color, false)
    }

    fun cut(guiGraphics: GuiGraphics, rect: Rect, runnable: Runnable) {
        guiGraphics.enableScissor(rect.lt.x.toInt(), rect.lt.y.toInt(), rect.rb.x.toInt(), rect.rb.y.toInt())
        runnable.run()
        guiGraphics.disableScissor()
    }

    private fun putUniform(
        std140LayoutName: String,
        uboSize: Int,
        put: (Std140Builder) -> Unit,
    ) {
        val ubo = ubo_map.getOrPut("$std140LayoutName-UBO") {
            MappableRingBuffer(
                { "$std140LayoutName-UBO" },
                GpuBuffer.USAGE_UNIFORM or GpuBuffer.USAGE_MAP_WRITE,
                uboSize,
            )
        }

        ubo.rotate()
        val gpuBuffer = ubo.currentBuffer()

        RenderSystem.getDevice().createCommandEncoder().mapBuffer(gpuBuffer, false, true).use { view ->
            val std140 = Std140Builder.intoBuffer(view.data())
            put(std140)
        }

        Uniforms.map[std140LayoutName] = gpuBuffer
    }
}