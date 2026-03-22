package haze.mixin;

import haze.event.impl.PacketEvent;
import haze.utility.MinecraftExtensionsKt;
import haze.utility.connection.PacketHandler;
import kotlin.Pair;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void callSendPacketEvent(Packet<?> packet, CallbackInfo ci) {
        if (true) return;
        ci.cancel();

        PacketEvent.Send.INSTANCE.setPacket(packet);
        PacketEvent.Send.INSTANCE.call();

        if (PacketEvent.Send.INSTANCE.getCanceled()) {
            return;
        }

        PacketHandler.INSTANCE.handleSentPacket(packet);
    }

    @Inject(method = "genericsFtw", at = @At("HEAD"), cancellable = true)
    private static void callReceivePacketEvent(Packet<?> packet, PacketListener packetListener, CallbackInfo ci) {
        PacketEvent.Receive packetEvent = new PacketEvent.Receive(packet);
        packetEvent.call();

        if (packetEvent.getCanceled())
            ci.cancel();
    }
}
