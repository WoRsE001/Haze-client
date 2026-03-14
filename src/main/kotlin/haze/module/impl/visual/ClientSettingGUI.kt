package haze.module.impl.visual

import haze.gui.clientsetting.ClientSettingScreen
import haze.key.Keybind
import haze.module.Category
import haze.module.Module
import haze.utility.mc
import org.lwjgl.glfw.GLFW

// created by dicves_recode on 16.12.2025
object ClientSettingGUI : Module(
    "ClientSettingGUI",
    Category.VISUAL,
    Keybind(GLFW.GLFW_KEY_RIGHT_SHIFT)
) {
    override fun toggle() {
        mc.setScreen(ClientSettingScreen)
    }
}
