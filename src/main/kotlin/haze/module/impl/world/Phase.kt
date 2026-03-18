package haze.module.impl.world

import haze.event.Event
import haze.event.impl.BlockShapeEvent
import haze.module.Category
import haze.module.Module
import haze.utility.mc
import haze.utility.player
import net.minecraft.world.phys.shapes.Shapes

// Blood! It's everywhere. SCWxD killed you on 18.03.2026 at 7:48.
object Phase : Module("Phase", Category.WORLD) {
    private val mode = list("Mode")
    private val noCollision by mode.subMode("No collision").select()

    override fun onEvent(event: Event) {
        if (event is BlockShapeEvent) {
            if (noCollision) {
                if (event.blockPos.y >= player.y || mc.options.keyShift.isDown && player.onGround())
                    event.shape = Shapes.empty()
            }
        }
    }
}