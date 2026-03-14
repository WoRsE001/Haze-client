package haze.setting.value

import haze.utility.math.Rect
import haze.utility.math.map
import haze.utility.math.roundTo
import haze.utility.mc
import kotlinx.serialization.json.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.MouseButtonEvent
import kotlin.math.abs

// испорченно SCWGxD в 31.01.2026:0:00
class NumberRangeValue(
    name: String,
    defaultValue: ClosedFloatingPointRange<Double>,
    val range: ClosedFloatingPointRange<Double>,
    val step: Double,
    val suffix: String = ""
) : Value<ClosedFloatingPointRange<Double>>(name, defaultValue) {
    override var rect = Rect(0f, 0f, 100f, 10f)

    var isDraggingMin = false
    var isDraggingMax = false

    override var json: JsonObject
        get() = buildJsonObject {
            put("Min", value.start)
            put("Max", value.endInclusive)
        }
        set(value) {
            val min = value["Min"]?.jsonPrimitive?.doubleOrNull ?: return
            val max = value["Max"]?.jsonPrimitive?.doubleOrNull ?: return

            this.value = min..max
        }

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        if (isDraggingMin) {
            value = map(mouseX.toDouble(), rect.lt.x.toDouble(), rect.rb.x.toDouble(), range.start, range.endInclusive).roundTo(step).coerceIn(range.start, value.endInclusive)..value.endInclusive
        } else if (isDraggingMax) {
            value = value.start..map(mouseX.toDouble(), rect.lt.x.toDouble(), rect.rb.x.toDouble(), range.start, range.endInclusive).roundTo(step).coerceIn(value.start, range.endInclusive)
        }

        guiGraphics.fill(rect.lt.x.toInt(), rect.lt.y.toInt(), rect.rb.x.toInt(), rect.rb.y.toInt(), 0xff2d2d2d.toInt())
        guiGraphics.fill(map(value.start, range.start, range.endInclusive, rect.lb.x.toDouble(), rect.rb.x.toDouble()).toInt(), rect.lt.y.toInt(), map(value.endInclusive, range.start, range.endInclusive, rect.lb.x.toDouble(), rect.rb.x.toDouble()).toInt(), rect.rb.y.toInt(), 0xff9c9c9c.toInt())
        val strValue = "${value.start.toString().format("%.${step.toString().length}f")}-${value.endInclusive.toString().format("%.${step.toString().length}f")}"
        guiGraphics.drawString(mc.font, strValue, (rect.center.x - mc.font.width(strValue) / 2).toInt(), (rect.center.y - mc.font.lineHeight / 2).toInt(), -1, false)
        guiGraphics.drawString(mc.font, name, (rect.rc.x + 1).toInt(), (rect.rc.y - mc.font.lineHeight / 2).toInt(), -1, false)
    }

    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
        val mouseX = mouseButtonEvent.x.toFloat()
        val mouseY = mouseButtonEvent.y.toFloat()
        val button = mouseButtonEvent.button()

        if (button == 0 && rect.isCollided(mouseX, mouseY)) {
            if (abs(mouseX - map(value.start - 1, range.start, range.endInclusive, rect.lb.x.toDouble(), rect.rb.x.toDouble())) <= abs(mouseX - map(value.endInclusive + 1, range.start, range.endInclusive, rect.lb.x.toDouble(), rect.rb.x.toDouble()))) {
                isDraggingMin = true
            } else {
                isDraggingMax = true
            }
            return true
        }

        return false
    }

    override fun mouseReleased(mouseButtonEvent: MouseButtonEvent): Boolean {
        isDraggingMin = false
        isDraggingMax = false
        return false
    }
}