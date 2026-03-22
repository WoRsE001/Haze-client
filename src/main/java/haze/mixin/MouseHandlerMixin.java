package haze.mixin;

import haze.event.impl.CursorDeltaEvent;
import com.llamalad7.mixinextras.sugar.Local;
import haze.utility.player.inventory.slot.FakeSlotStorageKt;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// испорченно SCWGxD в 16.12.2025:22:29
@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Shadow
    private double accumulatedDX;

    @Shadow
    private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"))
    private void turnPlayer(double timeDelta, CallbackInfo ci) {
        CursorDeltaEvent event = CursorDeltaEvent.INSTANCE;
        event.setAccumulatedDX(accumulatedDX);
        event.setAccumulatedDY(accumulatedDY);
        event.call();

        accumulatedDX = event.getAccumulatedDX();
        accumulatedDY = event.getAccumulatedDY();
    }

    @Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private void onScroll(long l, double d, double e, CallbackInfo ci, @Local() int k, @Local() Inventory inventory) {
        FakeSlotStorageKt.setFakeSelected(ScrollWheelHandler.getNextScrollWheelSelection(k, FakeSlotStorageKt.getFakeSelected(), Inventory.getSelectionSize()));
    }
}
