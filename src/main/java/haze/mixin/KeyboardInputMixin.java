package haze.mixin;

import haze.event.impl.MovementEvent;
import haze.event.impl.MovementInputEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// испорченно SCWGxD в 22.12.2025:20:54
@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends ClientInput {
    @ModifyExpressionValue(method = "tick", at = @At(value = "NEW", target = "(ZZZZZZZ)Lnet/minecraft/world/entity/player/Input;"))
    private Input callMovementInputEvent(Input original) {
        MovementInputEvent.INSTANCE.setForward(original.forward());
        MovementInputEvent.INSTANCE.setBack(original.backward());
        MovementInputEvent.INSTANCE.setLeft(original.left());
        MovementInputEvent.INSTANCE.setRight(original.right());
        MovementInputEvent.INSTANCE.setJump(original.jump());
        MovementInputEvent.INSTANCE.setSneak(original.shift());
        MovementInputEvent.INSTANCE.setSprint(original.sprint());
        MovementInputEvent.INSTANCE.call();

        return new Input(
                MovementInputEvent.INSTANCE.getForward(),
                MovementInputEvent.INSTANCE.getBack(),
                MovementInputEvent.INSTANCE.getLeft(),
                MovementInputEvent.INSTANCE.getRight(),
                MovementInputEvent.INSTANCE.getJump(),
                MovementInputEvent.INSTANCE.getSneak(),
                MovementInputEvent.INSTANCE.getSprint()
        );
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "NEW", target = "(FF)Lnet/minecraft/world/phys/Vec2;"))
    private Vec2 callMovementEvent(Vec2 original) {
        MovementEvent.INSTANCE.setMoveVector(original);
        MovementEvent.INSTANCE.call();

        return MovementEvent.INSTANCE.getMoveVector();
    }
}
