package haze.setting

import haze.utility.math.Rect
import haze.utility.mc
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.MouseButtonEvent

// created by dicves_recode on 09.01.2026
open class ToggleAbleConfigureAble(name: String, defaultToggled: Boolean) : ConfigureAble(name), ToggleAble {
    override var rect = Rect(0f, 0f, 10f, 10f)

    override var json: JsonObject
        get() = buildJsonObject {
            put("Info", buildJsonObject {
                put("Toggled", toggled)
            })

            put("Settings", super.json)
        }
        set(value) {
            val info = value["Info"]?.jsonObject ?: return
            val settings = value["Settings"]?.jsonObject ?: return

            toggled = info["Toggled"]?.jsonPrimitive?.booleanOrNull ?: return

            super.json = settings
        }

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        rect.size.y = 10f
        guiGraphics.fill(rect.lt.x.toInt(), rect.lt.y.toInt(), rect.rb.x.toInt(), rect.rb.y.toInt(), if (toggled) 0xff9c9c9c.toInt() else 0xff2d2d2d.toInt())
        val nameModificator = if (isShowSettings) "-" else "+"
        guiGraphics.drawString(mc.font, "$name $nameModificator", (rect.lc.x + 11).toInt(), (rect.lc.y - mc.font.lineHeight / 2).toInt(), -1, false)

        if (isShowSettings) {
            rect.size.y += 5
            for (item in value) {
                if (!item.visible.invoke()) continue
                item.rect.lt.x = rect.lb.x + 5
                item.rect.lt.y = rect.lb.y
                item.render(guiGraphics, mouseX, mouseY)
                rect.size.y += item.rect.size.y + 5
            }
            guiGraphics.fill((rect.lt.x + 1.5).toInt(), (rect.lt.y + 15).toInt(), (rect.lb.x + 2.5).toInt(), (rect.lb.y - 5).toInt(), -1)
        }
    }

    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
        val mouseX = mouseButtonEvent.x.toFloat()
        val mouseY = mouseButtonEvent.y.toFloat()
        val button = mouseButtonEvent.button()

        val nameModificator = if (isShowSettings) "-" else "+"
        if (button == 0) {
            if (rect.isCollided(mouseX, mouseY, rect.lt.x, rect.lt.y, 10f, 10f)) {
                toggle()
                return true
            } else if (value.isNotEmpty() && rect.isCollided(mouseX, mouseY, rect.lt.x + 11, rect.lt.y, mc.font.width("$name $nameModificator").toFloat(), 10f)) {
                isShowSettings = !isShowSettings
                return true
            }
        }

        if (isShowSettings) {
            for (item in value) {
                if (!item.visible.invoke()) continue
                if (item.mouseClicked(mouseButtonEvent, bl)) return true
            }
        }

        return false
    }

    override fun mouseReleased(mouseButtonEvent: MouseButtonEvent): Boolean {
        if (isShowSettings) {
            for (item in value) {
                if (!item.visible.invoke()) continue
                if (item.mouseReleased(mouseButtonEvent)) return true
            }
        }

        return false
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, hScroll: Double, vScroll: Double): Boolean {
        if (isShowSettings) {
            for (item in value) {
                if (!item.visible.invoke()) continue
                if (item.mouseScrolled(mouseX, mouseY, hScroll, vScroll)) return true
            }
        }

        return false
    }

    override var toggled = defaultToggled
        set(value) {
            if (field != value) {
                field = value

                if (field)
                    onEnable()
                else
                    onDisable()
            }
        }
}
