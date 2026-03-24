package haze.setting.preset

import haze.event.Event
import haze.event.impl.JumpEvent
import haze.event.impl.MoveRelativeEvent
import haze.event.impl.MovementEvent
import haze.setting.Configureable
import haze.utility.player.silentMoveFix
import haze.utility.player.rotation.CameraRotation

// Blood! It's everywhere. SCWxD killed you on 01.03.2026 at 7:15.
class MoveCorrector(name: String = "Move correct") : Configureable(name) {
    private val freeMoveCorrection by boolean("Free move correction", false)
    private val freeLegitMoveCorrection by boolean("Legit", false).visible { freeMoveCorrection }

    fun correctMove(event: Event) {
        if (freeMoveCorrection) {
            if (freeLegitMoveCorrection) {
                if (event is MovementEvent)
                    silentMoveFix(event)
            } else {
                if (event is JumpEvent.Pre)
                    event.yaw = CameraRotation.yaw

                if (event is MoveRelativeEvent)
                    event.yaw = CameraRotation.yaw
            }
        }
    }
}