package haze.event.impl

import haze.event.Event
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.AirBlock
import net.minecraft.world.phys.shapes.VoxelShape

// испорченно SCWGxD в 09.01.2026:10:09
object BlockShapeEvent : Event {
    var state = AirBlock.stateById(0)
    var blockPos = BlockPos.ZERO
    var shape: VoxelShape? = null
}