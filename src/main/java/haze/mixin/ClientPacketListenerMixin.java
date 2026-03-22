package haze.mixin;

import haze.event.impl.ChatMessageEvent;
import haze.utility.player.inventory.slot.FakeSlotStorageKt;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// created by dicves_recode on 28.12.2025
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleSetHeldSlot", at = @At("TAIL"))
    private void handleSetHeldSlot(ClientboundSetHeldSlotPacket clientboundSetHeldSlotPacket, CallbackInfo ci) {
        int slot = clientboundSetHeldSlotPacket.slot();

        if (Inventory.isHotbarSlot(slot))
            FakeSlotStorageKt.setFakeSelected(slot);
    }

    @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
    private void callChatSendEvent(String string, CallbackInfo ci) {
        ChatMessageEvent.SEND.INSTANCE.setContent(string);
        ChatMessageEvent.SEND.INSTANCE.call();
        if (ChatMessageEvent.SEND.INSTANCE.getCanceled())
            ci.cancel();
    }
}
