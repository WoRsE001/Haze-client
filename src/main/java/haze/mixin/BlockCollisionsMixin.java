package haze.mixin;

import haze.event.impl.BlockShapeEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// испорченно SCWGxD в 09.01.2026:10:14
@Mixin(BlockCollisions.class)
public class BlockCollisionsMixin {
    @Shadow
    @Final
    private BlockPos.MutableBlockPos pos;

    @ModifyExpressionValue(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/CollisionContext;getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape hookCollisionShape(VoxelShape original, @Local BlockState blockState) {
        if (this.pos == null)
            return original;

        BlockShapeEvent.INSTANCE.setState(blockState);
        BlockShapeEvent.INSTANCE.setBlockPos(this.pos);
        BlockShapeEvent.INSTANCE.setShape(original);
        BlockShapeEvent.INSTANCE.call();

        return BlockShapeEvent.INSTANCE.getShape();
    }
}
