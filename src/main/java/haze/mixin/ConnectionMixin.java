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
        PacketEvent.SEND.INSTANCE.setPacket(packet);
        PacketEvent.SEND.INSTANCE.setTotalDelay(0);
        PacketEvent.SEND.INSTANCE.call();

        PacketHandler.INSTANCE.getPacketQueue().removeIf(it -> {
            if (System.currentTimeMillis() - it.getSecond() >= PacketEvent.SEND.INSTANCE.getTotalDelay()) {
                MinecraftExtensionsKt.getConnection().getConnection().send(it.getFirst(), null);
                return true;
            }

            return false;
        });

        if (PacketEvent.SEND.INSTANCE.getCanceled()) {
            ci.cancel();
            return;
        }

        if (PacketEvent.SEND.INSTANCE.getTotalDelay() > 0) {
            ci.cancel();
            PacketHandler.INSTANCE.getPacketQueue().add(new Pair<>(packet, System.currentTimeMillis()));
        }
    }

    @Inject(method = "genericsFtw", at = @At("HEAD"), cancellable = true)
    private static void callReceivePacketEvent(Packet<?> packet, PacketListener packetListener, CallbackInfo ci) {
        PacketEvent.RECEIVE packetEvent = new PacketEvent.RECEIVE(packet);
        packetEvent.call();

        if (packetEvent.getCanceled())
            ci.cancel();
    }
}
