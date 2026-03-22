package haze.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.VertexFormat;
import haze.utility.render.Uniforms;
import net.minecraft.client.gui.render.GuiRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

// Blood! It's everywhere. SCWxD killed you on 22.03.2026 at 6:53.
@Mixin(GuiRenderer.class)
public class GuiRendererMixin {
    @Unique
    private final Uniforms uniforms = Uniforms.INSTANCE;

    @Inject(method = "executeDrawRange", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;bindDefaultUniforms(Lcom/mojang/blaze3d/systems/RenderPass;)V", shift = At.Shift.AFTER))
    private void setUniforms(Supplier<String> supplier, RenderTarget renderTarget, GpuBufferSlice gpuBufferSlice, GpuBufferSlice gpuBufferSlice2, GpuBuffer gpuBuffer, VertexFormat.IndexType indexType, int i, int j, CallbackInfo ci, @Local final RenderPass renderPass) {
        for (var u : uniforms.getMap().entrySet()) {
            renderPass.setUniform(u.getKey(), u.getValue());
        }
    }
}
