package haze.mixin;

import haze.module.impl.visual.AspectRatio;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

// испорченно SCWGxD в 27.12.2025:20:44
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyArgs(method = "getProjectionMatrix", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFF)Lorg/joml/Matrix4f;", remap = false))
    private void editMatrixToAspect(Args args) {
        if (AspectRatio.INSTANCE.getToggled()) {
            args.set(1, (float) args.get(1) / (float) AspectRatio.INSTANCE.getFactor());
        }
    }
}
