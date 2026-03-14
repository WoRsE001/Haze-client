package haze.mixin;

import haze.module.impl.player.Timer;
import haze.utility.MinecraftExtensionsKt;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// испорченно SCWGxD в 02.01.2026:14:42
@Mixin(DeltaTracker.Timer.class)
public class DeltaTrackerTimerMixin {
    @Shadow
    private float deltaTicks;

    @Inject(method = "advanceGameTime", at = @At(value = "FIELD", target = "Lnet/minecraft/client/DeltaTracker$Timer;deltaTicks:F", shift = At.Shift.AFTER))
    private void timer(long l, CallbackInfoReturnable<Integer> cir) {
        if (Timer.INSTANCE.getToggled())
            deltaTicks *= MinecraftExtensionsKt.getTimer(Minecraft.getInstance());
    }
}
