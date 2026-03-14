package haze.mixin;

import haze.mixin.accessor.LivingEntityAccessor;
import haze.module.impl.combat.KeepSprint;
import haze.module.impl.combat.Range;
import haze.module.impl.visual.InstantItemSwitch;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// created by dicves_recode on 28.12.2025
@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow
    public abstract void resetAttackStrengthTicker();

    @Inject(method = "causeExtraKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V"))
    private void keepSprintSlow(Entity entity, float f, Vec3 vec3, CallbackInfo ci) {
        if (KeepSprint.INSTANCE.getToggled()) {
            Player player = (Player) (Object) this;
            Vec3 normalMotion = player.getDeltaMovement().multiply(1 / 0.6, 1, 1 / 0.6);

            float slow = (float) (player.onGround() ? KeepSprint.INSTANCE.getGroundMotion() : KeepSprint.INSTANCE.getAirMotion());

            player.setDeltaMovement(normalMotion.multiply(slow, 1.0, slow));
        }
    }

    @Redirect(method = "causeExtraKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setSprinting(Z)V"))
    private void keepSprintSprint(Player instance, boolean b) {
        if (KeepSprint.INSTANCE.getToggled()) {
            instance.setSprinting(instance.onGround() ? KeepSprint.INSTANCE.getGroundSprint() : KeepSprint.INSTANCE.getAirSprint());
        }
    }

    @Redirect(
        method = "tick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"
        )
    )
    private void onResetAttackStrengthTicker(Player instance) {
        // Вместо вызова всего метода resetAttackStrengthTicker()
        // устанавливаем только attackStrengthTicker = 0
        if (InstantItemSwitch.INSTANCE.getToggled())
            ((LivingEntityAccessor) instance).setAttackStrengthTicker(0);
        else
            resetAttackStrengthTicker();
    }

    @ModifyExpressionValue(method = "entityInteractionRange", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    private double editEntityInteractionRange(double original) {
        return Range.INSTANCE.getToggled() ? Range.INSTANCE.getAttackReach() : original;
    }

    @ModifyExpressionValue(method = "blockInteractionRange", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    private double editBlockInteractionRange(double original) {
        return Range.INSTANCE.getToggled() ? Range.INSTANCE.getBlockReach() : original;
    }
}
