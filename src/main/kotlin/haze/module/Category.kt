package haze.module

import haze.gui.RenderAble
import haze.utility.math.Rect
import haze.utility.mc
import haze.utility.render.Render2D
import net.minecraft.client.gui.GuiGraphics

// created by dicves_recode on 29.11.2025
enum class Category : RenderAble {
    COMBAT,
    CONNECT,
    MISC,
    MOVE,
    PLAYER,
    VISUAL,
    WORLD;

    override var rect = Rect(0f, 0f, 50f, 9f)

    var scroll = 0f

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        Render2D.drawCentredText(guiGraphics, name, rect.center.x, rect.center.y, -1)
    }

    private val mutableModules = mutableSetOf<Module>()

    val modules: Set<Module>
        get() = mutableModules

    internal fun registerToCategory(module: Module) {
        require(module.category == this) { throw IllegalStateException("Module \"$module\" with category \"${module.category}\" cannot be registered in \"$this\"") }
        mutableModules.add(module)
    }
}
