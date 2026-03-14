package haze.module.impl.visual

import haze.module.Category
import haze.module.Module
import haze.utility.rotation.CameraRotation

// created by dicves_recode on 30.11.2025
object FreeLook : Module(
    "FreeLook",
    Category.VISUAL
) {
    private val rotateHeadBack by boolean("Rotate head back", true)

    override fun onEnable() {
        CameraRotation.unlocking = true
    }

    override fun onDisable() {
        if (rotateHeadBack)
            CameraRotation.unlocking = false
    }
}
