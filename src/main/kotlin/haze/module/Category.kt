package haze.module

import haze.gui.RenderAble
import haze.utility.math.Rect
import haze.utility.mc
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

    override var rect = Rect(0f, 0f, 100f, 35.7f)

    var scroll = 0f

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        guiGraphics.drawString(mc.font, name, (rect.center.x - mc.font.width(name) / 2).toInt(),(rect.center.y - mc.font.lineHeight / 2).toInt(), -1, false)
    }

    private val mutableModules = mutableSetOf<Module>()

    val modules: Set<Module>
        get() = mutableModules

    internal fun registerToCategory(module: Module) {
        require(module.category == this) { throw IllegalStateException("Module \"$module\" with category \"${module.category}\" cannot be registered in \"$this\"") }
        mutableModules.add(module)
    }
}
