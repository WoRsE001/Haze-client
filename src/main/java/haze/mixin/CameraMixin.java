package haze.mixin;

import haze.utility.player.rotation.CameraRotation;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// created by dicves_recode on 30.11.2025
@Mixin(Camera.class)
public class CameraMixin {
    @ModifyExpressionValue(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getViewXRot(F)F"))
    private float modifyPitch(float original) {
        return CameraRotation.INSTANCE.getPitch();
    }

    @ModifyExpressionValue(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getViewYRot(F)F"))
    private float modifyYaw(float original) {
        return CameraRotation.INSTANCE.getYaw();
    }
}
