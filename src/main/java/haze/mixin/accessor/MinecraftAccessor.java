package haze.mixin.accessor;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// испорченно SCWGxD в 14.12.2025:18:27
@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Invoker("startAttack")
    boolean invokeStartAttack();

    @Invoker("startUseItem")
    void invokeSartUseItem();
}
