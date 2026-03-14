package haze.utility.render

import haze.utility.math.Rect
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.gui.render.TextureSetup
import net.minecraft.client.gui.render.state.GuiElementRenderState
import org.joml.Matrix3x2fc

// Blood! It's everywhere. SCWxD killed you on 03.03.2026 at 12:33.
class RectRenderState(
    val renderPipeline: RenderPipeline,
    val textureSetup: TextureSetup,
    val pose: Matrix3x2fc,
    val rect: Rect,
    val colors: IntArray,
    val scissorArea: ScreenRectangle? = null,
    val bounds: ScreenRectangle? = null,
) : GuiElementRenderState {
    constructor(renderPipeline: RenderPipeline, pose: Matrix3x2fc, rect: Rect, color: Int) :
            this(renderPipeline, TextureSetup.noTexture(), pose, rect, intArrayOf(color, color, color, color))

    override fun buildVertices(vertexConsumer: VertexConsumer) {
        vertexConsumer.addVertexWith2DPose(pose, rect.lt.x, rect.lt.y).setColor(colors[0])
        vertexConsumer.addVertexWith2DPose(pose, rect.rt.x, rect.rt.y).setColor(colors[1])
        vertexConsumer.addVertexWith2DPose(pose, rect.rb.x, rect.rb.y).setColor(colors[2])
        vertexConsumer.addVertexWith2DPose(pose, rect.lb.x, rect.lb.y).setColor(colors[3])
    }

    override fun pipeline() = renderPipeline

    override fun textureSetup() = textureSetup

    override fun scissorArea() = scissorArea

    override fun bounds() = bounds
}