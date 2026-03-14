package haze.setting.value

import haze.utility.math.Rect
import haze.utility.math.map
import haze.utility.math.roundTo
import haze.utility.mc
import kotlinx.serialization.json.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.MouseButtonEvent

// испорченно SCWGxD в 30.01.2026:23:56
class NumberValue(
    name: String,
    defaultValue: Double,
    val range: ClosedRange<Double>,
    val step: Double,
    val suffix: String = ""
) : Value<Double>(name, defaultValue) {
    override var rect = Rect(0f, 0f, 100f, 10f)

    var isDragging = false

    override fun set(value: Double) {
        this.value = value.coerceIn(range.start, range.endInclusive).roundTo(step)
    }

    override var json: JsonObject
        get() = buildJsonObject {
            put("Value", value)
        }
        set(value) {
            this.value = value["Value"]?.jsonPrimitive?.doubleOrNull ?: return
        }

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        if (isDragging) {
            value = map(mouseX.toDouble(), rect.lt.x.toDouble(), rect.rb.x.toDouble(), range.start, range.endInclusive).coerceIn(range.start, range.endInclusive).roundTo(step)
        }

        guiGraphics.fill(rect.lt.x.toInt(), rect.lt.y.toInt(), rect.rb.x.toInt(), rect.rb.y.toInt(), 0xff2d2d2d.toInt())
        guiGraphics.fill(rect.lt.x.toInt(), rect.lt.y.toInt(), map(value, range.start, range.endInclusive, rect.lb.x.toDouble(), rect.rb.x.toDouble()).toInt(), rect.rb.y.toInt(), 0xff9c9c9c.toInt())
        val strValue = value.toString().format("%.${step.toString().length}f")
        guiGraphics.drawString(mc.font, strValue, (rect.center.x - mc.font.width(strValue) / 2).toInt(), (rect.center.y - mc.font.lineHeight / 2).toInt(), -1, false)
        guiGraphics.drawString(mc.font, name, (rect.rc.x + 1).toInt(), (rect.rc.y - mc.font.lineHeight / 2).toInt(), -1, false)
    }

    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
        val mouseX = mouseButtonEvent.x.toFloat()
        val mouseY = mouseButtonEvent.y.toFloat()
        val button = mouseButtonEvent.button()

        if (button == 0 && rect.isCollided(mouseX, mouseY)) {
            isDragging = true
            return true
        }

        return false
    }

    override fun mouseReleased(mouseButtonEvent: MouseButtonEvent): Boolean {
        isDragging = false
        return false
    }
}