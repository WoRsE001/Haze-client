package haze.utility.player

import haze.event.Event
import haze.event.impl.JumpEvent
import haze.event.impl.MoveRelativeEvent
import haze.event.impl.MovementEvent
import haze.setting.ConfigureAble
import haze.utility.rotation.CameraRotation

// Blood! It's everywhere. SCWxD killed you on 01.03.2026 at 7:15.
class MoveCorrectSetting : ConfigureAble("Move correct") {
    private val freeMoveCorrection by boolean("Free move correction", false)
    private val freeLegitMoveCorrection by boolean("Legit", false).visible { freeMoveCorrection }

    fun correctMove(event: Event) {
        if (freeMoveCorrection) {
            if (freeLegitMoveCorrection) {
                if (event is MovementEvent)
                    silentMoveFix(event)
            } else {
                if (event is JumpEvent)
                    event.yaw = CameraRotation.yaw

                if (event is MoveRelativeEvent)
                    event.yaw = CameraRotation.yaw
            }
        }
    }
}