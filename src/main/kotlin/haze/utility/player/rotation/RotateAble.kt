package haze.utility.player.rotation

// испорченно SCWGxD в 31.01.2026:22:36
interface RotateAble {
    val rotateOrdinal: Int

    fun rotate()
    fun shouldRotate(): Boolean
    fun registerToRotationHandler() = RotationHandler.register(this)
}