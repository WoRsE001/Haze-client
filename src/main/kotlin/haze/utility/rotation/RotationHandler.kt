package haze.utility.rotation

import haze.utility.player

// испорченно SCWGxD в 31.01.2026:22:37
object RotationHandler {
    private val rotateAbles = mutableListOf<RotateAble>()

    internal fun register(rotateAble: RotateAble) {
        rotateAbles.add(rotateAble)
        rotateAbles.sortBy { it.rotateOrdinal }
    }

    fun tick() {
        rotateAbles.forEach {
            if (it.shouldRotate()) {
                //CameraRotation.unlocking = true
                it.rotate()
                return
            }
        }

        if (CameraRotation.unlocking) {
            val delta = (CameraRotation - player.rotation).wrapped()

            if (delta.length() <= gcd()) {
                CameraRotation.unlocking = false
                return
            }

            player.xRot += delta.pitch
            player.xRot = player.xRot.coerceIn(-90f..90f)
            player.yRot += delta.yaw
        }
    }
}