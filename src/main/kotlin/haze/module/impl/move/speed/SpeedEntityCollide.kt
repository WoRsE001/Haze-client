package haze.module.impl.move.speed

import haze.event.Event
import haze.event.impl.PlayerStateUpdateEvent
import haze.setting.preset.EventListenerChoice
import haze.utility.level
import haze.utility.player
import haze.utility.player.getYawWithMovement
import haze.utility.player.isMoving
import haze.utility.player.velocityX
import haze.utility.player.velocityZ
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand
import kotlin.math.cos
import kotlin.math.sin

// Blood! It's everywhere. SCWxD killed you on 18.03.2026 at 10:43.
object SpeedEntityCollide : EventListenerChoice("Entity collide") {
    private val expandHitbox by number("Expand hitbox", 1.0, 0.0..1.0, 0.1)
    private val collideSpeed by number("Collide speed", 0.08, 0.0..0.2, 0.1)


    override fun onEvent(event: Event) {
        if (event is PlayerStateUpdateEvent.Pre) {
            if (!player.isMoving())
                return

            var collisions = 0
            val box = player.boundingBox.expandTowards(expandHitbox, expandHitbox, expandHitbox)

            for (entity in level.entitiesForRendering()) {
                val entityBox = entity.boundingBox

                if (entity != player && entity is LivingEntity && entity !is ArmorStand && box.intersects(entityBox)) {
                    collisions++
                }
            }

            val yaw = Math.toRadians(getYawWithMovement(player.yRot, player.input.keyPresses).toDouble())
            val boost = collideSpeed * collisions

            player.velocityX += -sin(yaw) * boost
            player.velocityZ += cos(yaw) * boost
        }
    }
}