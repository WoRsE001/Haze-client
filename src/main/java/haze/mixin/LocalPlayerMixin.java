package haze.mixin;

import haze.event.impl.SendPosEvent;
import haze.event.impl.SlowDownEvent;
import haze.event.impl.PlayerStateUpdateEvent;
import haze.module.impl.move.AutoSprint;
import haze.utility.player.PlayerStateUtilKt;
import haze.utility.player.rotation.CameraRotation;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// created by dicves_recode on 01.12.2025
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Shadow
    @Final
    protected Minecraft minecraft;
    @Unique
    SlowDownEvent slowDownConsumeEvent;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void resetCameraRotation(Minecraft minecraft, ClientLevel clientLevel, ClientPacketListener clientPacketListener, StatsCounter statsCounter, ClientRecipeBook clientRecipeBook, Input input, boolean bl, CallbackInfo ci) {
        CameraRotation.INSTANCE.setPitch(0f);
        CameraRotation.INSTANCE.setYaw(0f);
    }

    @ModifyExpressionValue(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"))
    private float modifyRenderPitch(float original) {
        return CameraRotation.INSTANCE.getPitch();
    }

    @ModifyExpressionValue(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"))
    private float modifyRenderYaw(float original) {
        return CameraRotation.INSTANCE.getYaw();
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Input;sprint()Z"))
    private boolean tickMovementModify(boolean original) {
        return AutoSprint.INSTANCE.getToggled() || original;
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void callUpdateEventPre(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;

        if (player.onGround()) {
            PlayerStateUtilKt.setUtilAirTicks(0);
            PlayerStateUtilKt.setUtilGroundTicks(PlayerStateUtilKt.getUtilGroundTicks() + 1);
        } else {
            PlayerStateUtilKt.setUtilAirTicks(PlayerStateUtilKt.getUtilAirTicks() + 1);
            PlayerStateUtilKt.setUtilGroundTicks(0);
        }

        PlayerStateUpdateEvent.Pre.INSTANCE.call();
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void callUpdateEventPost(CallbackInfo ci) {
        PlayerStateUpdateEvent.Post.INSTANCE.call();
    }

    @ModifyExpressionValue(method = "isSlowDueToUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/UseEffects;canSprint()Z"))
    private boolean isSlowDueToUsingItem(boolean original) {
        slowDownConsumeEvent = new SlowDownEvent(0.2, original);
        slowDownConsumeEvent.call();
        return slowDownConsumeEvent.getSprint();
    }

    @ModifyExpressionValue(method = "itemUseSpeedMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/UseEffects;speedMultiplier()F"))
    private float itemUseSpeedMultiplier(float original) {
        slowDownConsumeEvent = new SlowDownEvent(original, false);
        slowDownConsumeEvent.call();
        return (float) slowDownConsumeEvent.getSlowDown();
    }

    @Inject(method = "sendPosition", at = @At("HEAD"))
    private void callSendPositionEventPre(CallbackInfo ci) {
        SendPosEvent.Pre.INSTANCE.call();
    }

    @Inject(method = "sendPosition", at = @At("TAIL"))
    private void callSendPositionEventPost(CallbackInfo ci) {
        SendPosEvent.Post.INSTANCE.call();
    }
}
