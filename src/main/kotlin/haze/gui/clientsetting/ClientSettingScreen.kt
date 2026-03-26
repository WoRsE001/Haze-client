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
    private val rect = Rect(0f, 0f, 800f, 450f)
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
        if (d in rect.lt.x + 170..rect.lt.x + 420 && e in rect.lt.y + 95..rect.lt.y + 440) {
            for (module in selectedCategory.modules) {
                if (module.mouseScrolled(d, e, f, g))
                    return true
            }

            var totalHeight = 0f
            for (module in selectedCategory.modules) {
                totalHeight += module.rect.size.y
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
        Render2D.drawRect(guiGraphics, rect, 0xFF1D1B20.toInt())
        Render2D.drawRect(guiGraphics, rect.lt.x + 10, rect.lt.y + 10, 150f, 430f, 0xFF322F37.toInt())
        Render2D.drawRect(guiGraphics, rect.lt.x + 170, rect.lt.y + 95, 250f, 345f, 0xFF322F37.toInt())
        Render2D.drawRect(guiGraphics, rect.lt.x + 430, rect.lt.y + 95, 360f, 345f, 0xFF322F37.toInt())
        Render2D.drawRect(guiGraphics, rect.lt.x + 170, rect.lt.y + 10, 620f, 75f, 0xFF322F37.toInt())
        Render2D.drawText(
            guiGraphics,
            "Main",
            rect.lt.x + 85 - mc.font.width("Main") / 2,
            rect.lt.y + 90,
            0xFF1E1C21.toInt()
        )
    }

    fun renderProfile(guiGraphics: GuiGraphics) {
        Render2D.drawRect(guiGraphics, rect.lt.x + 20, rect.lt.y + 20, 130f, 60f, 0xFF2B2145.toInt())
    }

    fun renderCategories(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        var offsetY = rect.lt.y + 109
        for (category in Category.entries) {
            category.rect.lt.x = rect.lt.x + 20
            category.rect.lt.y = offsetY
            val categoryModule = if (category == selectedCategory) 0xFFD9D9D9.toInt() else 0xFF1E1C21.toInt()
            Render2D.drawText(guiGraphics, category.name, category.rect.lt.x, category.rect.lt.y, categoryModule)
            offsetY += category.rect.size.y + 10
        }
    }

    fun renderModules(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        var offsetY = rect.lt.y + 105 + selectedCategory.scroll
        for (module in selectedCategory.modules) {
            module.rect.lt.x = rect.lt.x + 180
            module.rect.lt.y = offsetY

            Render2D.cut(guiGraphics, Rect(rect.lt.x + 170, rect.lt.y + 95, 250f, 345f)) {
                Render2D.drawRect(guiGraphics, module.rect, 0xFF3F3B45.toInt())
                val moduleColor = if (module.toggled) 0xFFD9D9D9.toInt() else 0xFF1E1C21.toInt()
                Render2D.drawText(
                    guiGraphics,
                    module.name,
                    module.rect.lc.x + 10,
                    module.rect.lc.y - mc.font.lineHeight / 2,
                    moduleColor
                )
            }

            if (module.isShowSettings) {
                var offsetY = rect.lt.y + 105
                for (item in module.get()) {
                    if (!item.visible.invoke()) continue
                    item.rect.lt.x = rect.lt.x + 440
                    item.rect.lt.y = offsetY
                    item.render(guiGraphics, mouseX, mouseY)
                    offsetY += item.rect.size.y + 5
                }
            }

            offsetY += module.rect.size.y + 10
        }
    }
}