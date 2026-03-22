package haze.gui.clientsetting

import haze.HazeClient
import haze.module.Category
import haze.utility.math.Rect
import haze.utility.math.Vec2f
import haze.utility.mc
import haze.utility.render.Render2D
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component

// Blood! It's everywhere. SCWxD killed you on 04.03.2026 at 8:36.
object ClientSettingScreen : Screen(Component.empty()) {
    private val rect = Rect(0f, 0f, 400f, 300f)
    private var selectedCategory = Category.COMBAT

    init {
        rect.center = Vec2f(mc.window.guiScaledWidth / 2f, mc.window.guiScaledHeight / 2f)
    }

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val mouseX = i.toFloat()
        val mouseY = j.toFloat()

        renderBackground(guiGraphics)
        renderProfile(guiGraphics)
        renderCategories(guiGraphics, mouseX, mouseY)
        renderModules(guiGraphics, mouseX, mouseY)
    }

    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
        val mouseX = mouseButtonEvent.x.toFloat()
        val mouseY = mouseButtonEvent.y.toFloat()
        val button = mouseButtonEvent.button()

        for (category in Category.entries) {
            if (button == 0) {
                if (category.rect.isCollided(mouseX, mouseY)) {
                    selectedCategory = category
                    return true
                }
            }
        }

        if (mouseX in rect.lt.x + rect.size.x / 4f..rect.rb.x && mouseY in rect.lt.y + rect.size.y / 8f..rect.rb.y) {
            for (module in selectedCategory.modules) {
                if (module.mouseClicked(mouseButtonEvent, bl))
                    return true
            }
        }

        return false
    }

    override fun mouseReleased(mouseButtonEvent: MouseButtonEvent): Boolean {
        for (module in selectedCategory.modules) {
            if (module.mouseReleased(mouseButtonEvent))
                return true
        }

        return false
    }

    override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
        if (d in rect.lt.x + rect.size.x / 4f..rect.rb.x && e in rect.lt.y + rect.size.y / 8f..rect.rb.y) {
            for (module in selectedCategory.modules) {
                if (module.mouseScrolled(d, e, f, g))
                    return true
            }

            var totalHeight = 0f
            for (module in selectedCategory.modules) {
                totalHeight += module.getHeight()
            }

            if (totalHeight > rect.size.y / 6f * 5f) {
                selectedCategory.scroll += (g.toFloat() * 16)
            }

            selectedCategory.scroll = selectedCategory.scroll.coerceIn(-(totalHeight - 250f), 0f)
            return true
        }

        return false
    }

    fun renderBackground(guiGraphics: GuiGraphics) {
        Render2D.drawRect(guiGraphics, rect, 0xff3d3d3d.toInt())
        Render2D.drawRect(guiGraphics, Rect(rect.lt.x, rect.lt.y, rect.size.x / 4f, rect.size.y), 0xff292929.toInt())
        Render2D.drawRect(guiGraphics, Rect(rect.lt.x, rect.lt.y + rect.size.x / 8f, rect.size.x / 4f, 2f), 0xff000000.toInt())
    }

    fun renderProfile(guiGraphics: GuiGraphics) {
        Render2D.drawCentredText(guiGraphics, HazeClient.NAME, rect.lt.x + rect.size.x / 8f, rect.lt.y + rect.size.y / 12f, -1)
    }

    fun renderCategories(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        var offsetY = rect.lt.y + rect.size.y / 6f
        for (category in Category.entries) {
            if (category == selectedCategory) {
                val rectSelectedCategory = Rect(rect.lt.x + 5f, offsetY + 5f, rect.size.x / 4f - 10f, rect.size.y / 6f * 5f / Category.entries.size - 10f)

                Render2D.drawRect(guiGraphics, rectSelectedCategory, 0x80ff8000.toInt())
            }

            category.rect = Rect(rect.lt.x, offsetY, rect.size.x / 4f, rect.size.y / 6f * 5f / Category.entries.size)
            category.render(guiGraphics, mouseX, mouseY)
            offsetY += category.rect.size.y
        }
    }

    fun renderModules(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        Render2D.cut(guiGraphics, Rect(rect.lt.x + rect.size.x / 4f, rect.lt.y + rect.size.y / 6f, rect.size.x / 4f * 3f, rect.size.y / 6f * 5f)) {
            var offsetY = rect.lt.y + rect.size.y / 6f + selectedCategory.scroll
            for (module in selectedCategory.modules) {
                module.rect = Rect(rect.lt.x + rect.size.x / 4f, offsetY, rect.size.x / 4f * 3f, rect.size.y / 6f)
                module.render(guiGraphics, mouseX, mouseY)
                offsetY += module.getHeight()
            }
        }
    }
}