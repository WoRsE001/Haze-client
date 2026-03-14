package haze.utility.render

import haze.utility.math.Vec3f
import haze.utility.mc
import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.phys.AABB
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import java.util.OptionalDouble
import java.util.OptionalInt
import kotlin.use

// Blood! It's everywhere. SCWxD killed you on 27.02.2026 at 11:14.
object Render3D {
    private val COLOR_MODULATOR = Vector4f(1f, 1f, 1f, 1f)
    private val MODEL_OFFSET = Vector3f(0f, 0f, 0f)
    private val TEXTURE_MATRIX = Matrix4f()

    val espPipeline by lazy {
        RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                .withLocation(Identifier.fromNamespaceAndPath("hesh_client", "esp_pipeline"))
                .withBlend(BlendFunction.TRANSLUCENT)
                .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                .withDepthWrite(false)
                .withCull(false)
                .build()
        )
    }

    private val lines = mutableListOf<Pair<Vec3f, Vec3f>>()
    private var currentColor = floatArrayOf(1f, 1f, 1f, 1f)
    private var currentLineWidth = 2f

    fun drawBox(poseStack: PoseStack, box: AABB, r: Float, g: Float, b: Float, a: Float, lineWidth: Float = 2f) {
        val minX = box.minX.toFloat()
        val minY = box.minY.toFloat()
        val minZ = box.minZ.toFloat()
        val maxX = box.maxX.toFloat()
        val maxY = box.maxY.toFloat()
        val maxZ = box.maxZ.toFloat()

        currentColor = floatArrayOf(r, g, b, a)
        currentLineWidth = lineWidth
        lines.clear()
        
        addLine(minX, minY, minZ, maxX, minY, minZ)
        addLine(maxX, minY, minZ, maxX, minY, maxZ)
        addLine(maxX, minY, maxZ, minX, minY, maxZ)
        addLine(minX, minY, maxZ, minX, minY, minZ)

        addLine(minX, maxY, minZ, maxX, maxY, minZ)
        addLine(maxX, maxY, minZ, maxX, maxY, maxZ)
        addLine(maxX, maxY, maxZ, minX, maxY, maxZ)
        addLine(minX, maxY, maxZ, minX, maxY, minZ)

        renderLines(poseStack)
    }

    fun drawCornerBoxRGBA(
        matrices: PoseStack,
        box: AABB,
        r: Float, g: Float, b: Float, a: Float,
        length: Float = 0.25f,
        lineWidth: Float = 3f
    ) {
        val minX = box.minX.toFloat()
        val minY = box.minY.toFloat()
        val minZ = box.minZ.toFloat()
        val maxX = box.maxX.toFloat()
        val maxY = box.maxY.toFloat()
        val maxZ = box.maxZ.toFloat()

        val wX = (maxX - minX) * length
        val hY = (maxY - minY) * length
        val dZ = (maxZ - minZ) * length

        currentColor = floatArrayOf(r, g, b, a)
        currentLineWidth = lineWidth
        lines.clear()

        // Bottom corners
        addLine(minX, minY, minZ, minX + wX, minY, minZ)
        addLine(minX, minY, minZ, minX, minY + hY, minZ)
        addLine(minX, minY, minZ, minX, minY, minZ + dZ)

        addLine(maxX, minY, minZ, maxX - wX, minY, minZ)
        addLine(maxX, minY, minZ, maxX, minY + hY, minZ)
        addLine(maxX, minY, minZ, maxX, minY, minZ + dZ)

        addLine(minX, minY, maxZ, minX + wX, minY, maxZ)
        addLine(minX, minY, maxZ, minX, minY + hY, maxZ)
        addLine(minX, minY, maxZ, minX, minY, maxZ - dZ)

        addLine(maxX, minY, maxZ, maxX - wX, minY, maxZ)
        addLine(maxX, minY, maxZ, maxX, minY + hY, maxZ)
        addLine(maxX, minY, maxZ, maxX, minY, maxZ - dZ)

        // Top corners
        addLine(minX, maxY, minZ, minX + wX, maxY, minZ)
        addLine(minX, maxY, minZ, minX, maxY - hY, minZ)
        addLine(minX, maxY, minZ, minX, maxY, minZ + dZ)

        addLine(maxX, maxY, minZ, maxX - wX, maxY, minZ)
        addLine(maxX, maxY, minZ, maxX, maxY - hY, minZ)
        addLine(maxX, maxY, minZ, maxX, maxY, minZ + dZ)

        addLine(minX, maxY, maxZ, minX + wX, maxY, maxZ)
        addLine(minX, maxY, maxZ, minX, maxY - hY, maxZ)
        addLine(minX, maxY, maxZ, minX, maxY, maxZ - dZ)

        addLine(maxX, maxY, maxZ, maxX - wX, maxY, maxZ)
        addLine(maxX, maxY, maxZ, maxX, maxY - hY, maxZ)
        addLine(maxX, maxY, maxZ, maxX, maxY, maxZ - dZ)

        renderLines(matrices)
    }

    private fun addLine(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float) {
        lines += Pair(Vec3f(x1, y1, z1), Vec3f(x2, y2, z2))
    }

    private fun renderLines(matrices: PoseStack) {
        if (lines.isEmpty()) return

        val mat = matrices.last().pose()
        val tess = Tesselator.getInstance()
        val buf = tess.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH)

        val r = currentColor[0]
        val g = currentColor[1]
        val b = currentColor[2]
        val a = currentColor[3]
        val lw = currentLineWidth

        for (line in lines) {
            val camPos = mc.gameRenderer.mainCamera.position()
            val pos1 = line.first - Vec3f(camPos.x, camPos.y, camPos.z)
            val pos2 = line.second - Vec3f(camPos.x, camPos.y, camPos.z)
            val normDiffPos = (pos2 - pos1).normalized()

            buf.addVertex(mat, pos1.x, pos1.y, pos1.z).setColor(r, g, b, a).setNormal(normDiffPos.x, normDiffPos.y, normDiffPos.z).setLineWidth(lw)
            buf.addVertex(mat, pos2.x, pos2.y, pos2.z).setColor(r, g, b, a).setNormal(normDiffPos.x, normDiffPos.y, normDiffPos.z).setLineWidth(lw)
        }

        flush(buf)
        lines.clear()
    }

    private fun flush(buf: BufferBuilder) {
        val built = try { buf.buildOrThrow() } catch (_: Exception) { return }

        val byteBuffer = built.vertexBuffer()
        val vertexCount = built.drawState().vertexCount()

        if (vertexCount == 0) {
            built.close()
            return
        }

        val gpuVbo = RenderSystem.getDevice().createBuffer(
            { "hash_client:esp_vbo" },
            GpuBuffer.USAGE_VERTEX,
            byteBuffer
        )

        val dynTransforms = RenderSystem.getDynamicUniforms().writeTransform(
            RenderSystem.getModelViewMatrix(),
            COLOR_MODULATOR,
            MODEL_OFFSET,
            TEXTURE_MATRIX
        )

        val encoder = RenderSystem.getDevice().createCommandEncoder()
        encoder.createRenderPass(
            { "hash_client:esp_lines" },
            mc.mainRenderTarget.colorTextureView!!,
            OptionalInt.empty(),
            null,
            OptionalDouble.empty()
        ).use { pass ->
            pass.setPipeline(espPipeline)
            pass.setVertexBuffer(0, gpuVbo)
            RenderSystem.bindDefaultUniforms(pass)
            pass.setUniform("DynamicTransforms", dynTransforms)
            pass.draw(0, vertexCount)
        }

        built.close()
        gpuVbo.close()
    }
}