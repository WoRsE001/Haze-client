package haze.setting.value

import haze.utility.math.Rect
import haze.utility.mc
import kotlinx.serialization.json.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.MouseButtonEvent

// испорченно SCWGxD в 30.01.2026:23:54
class BooleanValue(name: String, defaultValue: Boolean) : Value<Boolean>(name, defaultValue) {
    override var rect = Rect(0f, 0f, 10f, 10f)

    override var json: JsonObject
        get() = buildJsonObject {
            put("Toggled", value)
        }
        set(value) {
            this.value = value["Toggled"]?.jsonPrimitive?.booleanOrNull ?: return
        }

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        guiGraphics.fill(rect.lt.x.toInt(), rect.lt.y.toInt(), rect.rb.x.toInt(), rect.rb.y.toInt(), if (value) 0xff9c9c9c.toInt() else 0xff2d2d2d.toInt())
        guiGraphics.drawString(mc.font, name, (rect.rc.x + 1).toInt(), (rect.rc.y - mc.font.lineHeight / 2).toInt(), -1, false)
    }

    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
        if (rect.isCollided(mouseButtonEvent.x.toFloat(), mouseButtonEvent.y.toFloat())) {
            value = !value
            return true
        }

        return false
    }
}