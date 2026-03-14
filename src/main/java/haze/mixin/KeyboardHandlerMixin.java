package haze.mixin;

import haze.utility.MinecraftExtensionsKt;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// created by dicves_recode on 28.11.2025
@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void callKeyEvent(long l, int i, KeyEvent keyEvent, CallbackInfo ci) {
        if (l == MinecraftExtensionsKt.getMc().getWindow().handle()) {
            haze.event.impl.KeyEvent event = new haze.event.impl.KeyEvent(keyEvent.key(), i, keyEvent.modifiers());
            event.call();
        }
    }
}
