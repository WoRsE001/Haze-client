package haze.mixin;

import haze.event.impl.JumpEvent;
import haze.module.impl.move.NoJumpDelay;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// испорченно SCWGxD в 18.12.2025:16:15
@Mixin(LivingEntity.class)
public abstract class LivinEntityMixin {
    @Shadow
    private int noJumpDelay;

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void aiStepHook(CallbackInfo ci) {
        if (NoJumpDelay.INSTANCE.getToggled())
            this.noJumpDelay = 0;
    }

    @Inject(method = "jumpFromGround", at = @At("HEAD"), cancellable = true)
    private void callJumpEventPre(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity != Minecraft.getInstance().player)
            return;

        JumpEvent.Pre event = JumpEvent.Pre.INSTANCE;
        event.setYaw(entity.getYRot());
        event.setHeight(0.42f);
        event.call();

        if (event.getCanceled())
            ci.cancel();
    }

    @ModifyExpressionValue(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getYRot()F"))
    private float modifyJumpYaw(float original) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity != Minecraft.getInstance().player)
            return original;

        return JumpEvent.Pre.INSTANCE.getYaw();
    }

    @ModifyExpressionValue(method = "getJumpPower(F)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    private double modifyJumpHeight(double original) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity != Minecraft.getInstance().player)
            return original;

        return JumpEvent.Pre.INSTANCE.getHeight();
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void callJumpEventPost(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity != Minecraft.getInstance().player)
            return;

        JumpEvent.Post.INSTANCE.call();
    }
}
