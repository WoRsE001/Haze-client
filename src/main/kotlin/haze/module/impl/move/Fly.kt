package haze.module.impl.move

import haze.event.Event
import haze.event.impl.BlockShapeEvent
import haze.event.impl.PlayerStateUpdateEvent
import haze.module.Category
import haze.module.Module
import haze.utility.mc
import haze.utility.player
import haze.utility.player.withStrafe
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.Shapes

// испорченно SCWGxD в 22.12.2025:15:31
object Fly : Module(
    "Fly",
    Category.MOVE
) {
    private val mode = list("Mode")

    private val modeMotion = mode.choice("Motion").select()
    private val speedXZ by number("Speed XZ", 1.0, 0.0..10.0, 0.01).visible { modeMotion.selected() }
    private val speedY by number("Speed Y", 1.0, 0.0..10.0, 0.01).visible { modeMotion.selected() }

    private val modeAirWalk = mode.choice("AirWalk")
    private val sameY by boolean("Same Y", false).visible { modeAirWalk.selected() }

    private var savedY = 0

    override fun onEnable() {
        savedY = player.blockPosition().y
    }

    override fun onEvent(event: Event) {
        if (event is BlockShapeEvent) {
            if (modeAirWalk.selected()) {
                if (!sameY)
                    savedY = player.blockPosition().y

                if (event.blockPos.y < savedY && (sameY || !mc.options.keyShift.isDown || !player.onGround()))
                    event.shape = Shapes.block()
            }
        }

        if (event is PlayerStateUpdateEvent.Pre) {
            if (modeMotion.selected()) {
                player.deltaMovement = player.deltaMovement.multiply(Vec3(0.0, 0.0, 0.0))

                player.deltaMovement = player.deltaMovement.withStrafe(speedXZ)

                if (mc.options.keyJump.isDown)
                    player.deltaMovement = player.deltaMovement.add(0.0, speedY, 0.0)
                if (mc.options.keyShift.isDown)
                    player.deltaMovement = player.deltaMovement.add(0.0, -speedY, 0.0)
            }
        }
    }
}