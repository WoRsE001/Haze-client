package haze.gui.clientsetting

import haze.HazeClient
import haze.module.Category
import haze.utility.mc
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component

// Blood! It's everywhere. SCWxD killed you on 04.03.2026 at 8:36.
object ClientSettingScreen : Screen(Component.empty()) {
    private var selectedCategory = Category.COMBAT
    private var offsetY = 0f

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        // Background
        guiGraphics.fill(0, 0, 400, 300, 0xff3d3d3d.toInt())
        guiGraphics.fill(0, 0, 100, 300, 0xff292929.toInt())
        guiGraphics.fill(0, 50, 100, 52, 0xff000000.toInt())
        guiGraphics.drawString(mc.font, HazeClient.NAME, 50 - mc.font.width(HazeClient.NAME) / 2, 25 - mc.font.lineHeight / 2, -1, false)

        // Categories
        offsetY = 50f
        for (category in Category.entries) {
            if (category == selectedCategory)
                guiGraphics.fill(5, (category.rect.size.y * 0.1 + offsetY).toInt(), 95, (category.rect.size.y * 0.9 + offsetY).toInt(), 0x80ff8000.toInt())

            category.rect.lt.x = 0f
            category.rect.lt.y = offsetY
            category.render(guiGraphics, i.toFloat(), j.toFloat())
            offsetY += category.rect.size.y
        }

        // Modules
        guiGraphics.enableScissor(101, 51, 400, 300)
        offsetY = 50f + selectedCategory.scroll
        for (module in selectedCategory.modules) {
            module.rect.lt.x = 100f
            module.rect.lt.y = offsetY
            module.render(guiGraphics, i.toFloat(), j.toFloat())
            offsetY += module.getHeight()
        }
        guiGraphics.disableScissor()
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

        if (mouseX in 101f..400f && mouseY in 51f..300f) {
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
        if (d in 101f..400f && e in 51f..300f) {
            for (module in selectedCategory.modules) {
                if (module.mouseScrolled(d, e, f, g))
                    return true
            }

            var totalHeight = 0f
            for (module in selectedCategory.modules) {
                totalHeight += module.getHeight()
            }

            if (totalHeight > 250f) {
                selectedCategory.scroll += (g.toFloat() * 16)
            }

            selectedCategory.scroll = selectedCategory.scroll.coerceIn(-(totalHeight - 250f), 0f)
            return true
        }

        return false
    }
}