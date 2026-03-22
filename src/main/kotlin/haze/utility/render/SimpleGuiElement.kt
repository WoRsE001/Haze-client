package haze.utility.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.gui.render.state.GuiElementRenderState

// Blood! It's everywhere. SCWxD killed you on 21.03.2026 at 4:57.
class SimpleGuiElement(
    private val guiGraphics: GuiGraphics,
    private val pipeline: RenderPipeline,
    private val textureSetup: TextureSetup,
    private val x: Float,
    private val y: Float,
    private val width: Float,
    private val height: Float,
    private val vertexConsumer: (VertexConsumer) -> Unit,
) : GuiElementRenderState {

    override fun buildVertices(vertexConsumer: VertexConsumer) = this.vertexConsumer(vertexConsumer)

    override fun pipeline() = pipeline

    override fun textureSetup() = textureSetup

    override fun scissorArea() = guiGraphics.scissorStack.peek()

    override fun bounds(): ScreenRectangle {
        val screenRect = ScreenRectangle(x.toInt(), y.toInt(), width.toInt(), height.toInt()).transformMaxBounds(guiGraphics.pose())

        return guiGraphics.scissorStack.peek()?.intersection(screenRect) ?: screenRect
    }
}