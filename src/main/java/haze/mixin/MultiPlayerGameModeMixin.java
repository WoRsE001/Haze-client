package haze.mixin;

import haze.event.impl.AttackEvent;
import haze.utility.target.TargetSearchUtilsKt;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// испорченно SCWGxD в 22.12.2025:14:33
@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void callAttackPREEvent(Player player, Entity entity, CallbackInfo ci) {
        AttackEvent.PRE attackEvent = new AttackEvent.PRE(entity);
        attackEvent.call();

        if (attackEvent.getCanceled()) {
            ci.cancel();
            return;
        }

        if (entity instanceof LivingEntity livingEntity)
            TargetSearchUtilsKt.setLastAttackedTarget(livingEntity);
    }



    @Inject(method = "attack", at = @At("TAIL"))
    private void callAttackPOSTEvent(Player player, Entity entity, CallbackInfo ci) {
        AttackEvent.POST.INSTANCE.call();
    }
}
