package haze.mixin.accessor;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// created by dicves_recode on 28.12.2025
@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("attackStrengthTicker")
    void setAttackStrengthTicker(int attackStrengthTicker);

    @Accessor("itemSwapTicker")
    void setItemSwapTicker(int itemSwapTicker);
}
