package haze.mixin;

import haze.event.impl.MoveRelativeEvent;
import haze.utility.MinecraftExtensionsKt;
import haze.utility.rotation.CameraRotation;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// created by dicves_recode on 01.12.2025
@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    private float yRot;

    @Shadow
    private float xRot;

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    private void unlockCamera(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if ((Object) this != MinecraftExtensionsKt.getMc().player)
            return;

        float pitchDelta = (float)cursorDeltaY * 0.15F;
        float yawDelta = (float)cursorDeltaX * 0.15F;

        if (CameraRotation.INSTANCE.getUnlocking()) {
            CameraRotation.INSTANCE.setPitch(Math.clamp(CameraRotation.INSTANCE.getPitch() + pitchDelta, -90, 90));
            CameraRotation.INSTANCE.setYaw(CameraRotation.INSTANCE.getYaw() + yawDelta);
            ci.cancel();
        }
    }

    @Inject(method = "turn", at = @At("TAIL"))
    private void setCameraRotation(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        CameraRotation rotation = CameraRotation.INSTANCE;

        if (!rotation.getUnlocking()) {
            rotation.setPitch(xRot);
            rotation.setYaw(rotation.getYaw() + Mth.wrapDegrees(yRot - rotation.getYaw()));
        }
    }

    @ModifyExpressionValue(method = "isLocalInstanceAuthoritative", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isClientAuthoritative()Z"))
    private boolean fixFallDistanceDidiBludCalculation(boolean original) {
        if ((Object) this == MinecraftExtensionsKt.getMc().player)
            return false;

        return original;
    }

    @Inject(method = "moveRelative", at = @At("HEAD"))
    private void callMoveRelativeEvent(float f, Vec3 vec3, CallbackInfo ci) {
        if ((Object) this != Minecraft.getInstance().player)
            return;

        MoveRelativeEvent event = MoveRelativeEvent.INSTANCE;
        event.setYaw(yRot);
        event.setInput(vec3);
        event.call();
    }

    @ModifyExpressionValue(method = "moveRelative", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getYRot()F"))
    private float modifyMoveRelativeGetYRot(float original) {
        if ((Object) this != Minecraft.getInstance().player)
            return original;

        return MoveRelativeEvent.INSTANCE.getYaw();
    }
}
