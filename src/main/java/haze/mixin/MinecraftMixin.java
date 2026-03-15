package haze.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import haze.HazeClient;
import haze.event.impl.GameLoopEvent;
import haze.event.impl.LegitClickTimingEvent;
import haze.event.impl.TickEvent;
import haze.module.impl.misc.Debug;
import haze.module.impl.visual.HitAccuracy;
import haze.utility.MinecraftExtensionsKt;
import haze.utility.rotation.RotationHandler;
import haze.utility.slot.FakeSlotStorageKt;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// created by dicves_recode on 28.11.2025
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Final
    private Window window;

    @Shadow
    public int missTime;

    @Shadow
    @Nullable
    public LocalPlayer player;

    @ModifyExpressionValue(method = "createTitle", at = @At(value = "NEW", target = "(Ljava/lang/String;)Ljava/lang/StringBuilder;"))
    private StringBuilder editMCTitle(StringBuilder original) {
        return new StringBuilder(HazeClient.NAME + " client");
    }

    @ModifyExpressionValue(method = "createTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ModCheck;shouldReportAsModified()Z"))
    private boolean editModifesgsghrd(boolean original) {
        return false;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void callPreTickEvent(CallbackInfo ci) {
        TickEvent.PRE event = TickEvent.PRE.INSTANCE;
        event.call();

        if (MinecraftExtensionsKt.nullCheck())
            RotationHandler.INSTANCE.tick();

        if (event.getCanceled())
            ci.cancel();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void callPostTickEvent(CallbackInfo ci) {
        TickEvent.POST event = TickEvent.POST.INSTANCE;
        event.call();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void callRunGameLoopEvent(boolean tick, CallbackInfo ci) {
        GameLoopEvent.INSTANCE.call();
    }

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal = 14, shift = At.Shift.BEFORE))
    private void callLegitClickTimingEvent(CallbackInfo ci) {
        LegitClickTimingEvent.INSTANCE.call();
    }

    @Inject(method = "startAttack", at = @At("HEAD"))
    private void noHitDelayFix(CallbackInfoReturnable<Boolean> cir) {
        this.missTime = 0;
        Debug.INSTANCE.getCPSArray()[MinecraftExtensionsKt.getPlayer().tickCount % 10] += 1;

        if (HitAccuracy.INSTANCE.getToggled())
            HitAccuracy.INSTANCE.handleAttack();
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void closeClient(CallbackInfo ci) {
        HazeClient.INSTANCE.close();
    }

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private void handleKeybinds(CallbackInfo ci, @Local() int i) {
        FakeSlotStorageKt.setFakeSelected(i);
    }
}
