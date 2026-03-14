package haze.mixin.accessor;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// created by dicves_recode on 05.12.2025
@Mixin(MultiPlayerGameMode.class)
public interface MultiPlayerGameModeAccessor {
    @Accessor("destroyDelay")
    int getDestroyDelay();

    @Accessor("destroyDelay")
    void setDestroyDelay(int destroyDelay);

    @Accessor("destroyProgress")
    float getDestroyProgress();

    @Accessor("destroyProgress")
    void setDestroyProgress(float destroyProgress);
}
