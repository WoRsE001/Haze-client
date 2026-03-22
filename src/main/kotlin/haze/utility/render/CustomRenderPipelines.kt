package haze.utility.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.shaders.UniformType
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import haze.HazeClient
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

// Blood! It's everywhere. SCWxD killed you on 03.03.2026 at 12:41.
object CustomRenderPipelines {
    val RECT: RenderPipeline = RenderPipelines.register(
        RenderPipeline.builder()
        .withFragmentShader(HazeClient.of("core/pos_col"))
        .withVertexShader(HazeClient.of("core/pos_col"))
        .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
        .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
        .withLocation(HazeClient.of("rect"))
        .build()
    )
}