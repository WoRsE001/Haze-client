package haze.mixin;

import haze.module.impl.visual.InstantItemSwitch;
import haze.utility.rotation.CameraRotation;
import haze.utility.slot.FakeSlotStorageKt;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// created by dicves_recode on 01.12.2025
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private float mainHandHeight;

    @Shadow
    private ItemStack mainHandItem;

    @Shadow
    private ItemStack offHandItem;

    @ModifyExpressionValue(method = "renderHandsWithItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewXRot(F)F"))
    private float modifyRenderPitch(float original) {
        return CameraRotation.INSTANCE.getPitch();
    }

    @ModifyExpressionValue(method = "renderHandsWithItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot(F)F"))
    private float modifyLerpedPitch(float original) {
        return CameraRotation.INSTANCE.getPitch();
    }

    @ModifyExpressionValue(method = "renderHandsWithItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewYRot(F)F"))
    private float modifyRenderYaw(float original) {
        return CameraRotation.INSTANCE.getYaw();
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getMainHandItem()Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack modifyItemInHand(ItemStack original) {
        assert minecraft.player != null;
        return minecraft.player.getInventory().getItem(FakeSlotStorageKt.getFakeSelected());
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;shouldInstantlyReplaceVisibleItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean removeItemSwitchAnimation(boolean original) {
        if (InstantItemSwitch.INSTANCE.getToggled())
            return true;

        return original;
    }
}
