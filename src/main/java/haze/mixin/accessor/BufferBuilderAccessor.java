package haze.mixin.accessor;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// Blood! It's everywhere. SCWxD killed you on 03.03.2026 at 12:43.
@Mixin(BufferBuilder.class)
public interface BufferBuilderAccessor {
    @Invoker("beginElement")
    long begin(VertexFormatElement format);
}
