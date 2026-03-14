package haze.setting.value

import haze.setting.value.ChoiceValue.SubMode
import haze.utility.math.Rect
import haze.utility.mc
import kotlinx.serialization.json.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.MouseButtonEvent
import kotlin.reflect.KProperty

// испорченно SCWGxD в 31.01.2026:9:45
class ChoiceValue(name: String) : Value<SubMode?>(name, null) {
    override var rect = Rect(0f, 0f, 0f, 0f)

    val list = mutableListOf<SubMode>()
    var isShowChoices = false

    fun subMode(name: String) = SubMode(name, this)

    override var json: JsonObject
        get() = buildJsonObject {
            put("Choice", value?.name)
        }
        set(value) {
            val name = value["Choice"]?.jsonPrimitive?.contentOrNull ?: return
            this.value = list.first { it.name == name }
        }

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        val nameModificator = if (isShowChoices) "-" else "+"
        val sizeX = list.maxOfOrNull { mc.font.width(it.name) }?.plus(mc.font.width("$name:  $nameModificator"))
        rect.size.x = sizeX?.toFloat()!!
        rect.size.y = 10f * if (isShowChoices) list.size + 1 else 1

        guiGraphics.fill(rect.lt.x.toInt(), rect.lt.y.toInt(), rect.rb.x.toInt(), rect.rb.y.toInt(), 0xff2d2d2d.toInt())
        guiGraphics.drawString(mc.font, "$name: ${value?.name}", rect.lt.x.toInt(), (rect.lt.y + 5 - mc.font.lineHeight / 2).toInt(), -1, false)
        guiGraphics.drawString(mc.font, nameModificator, (rect.rt.x - mc.font.width(nameModificator)).toInt(), (rect.rt.y + 5 - mc.font.lineHeight / 2).toInt(), -1, false)

        if (isShowChoices) {
            var offsetY = 10f
            for (mode in list) {
                guiGraphics.drawString(mc.font, mode.name, rect.lt.x.toInt(), (rect.lt.y + 5 - mc.font.lineHeight / 2 + offsetY).toInt(), -1, false)
                offsetY += 10f
            }
        }
    }

    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
        val mouseX = mouseButtonEvent.x.toFloat()
        val mouseY = mouseButtonEvent.y.toFloat()
        val button = mouseButtonEvent.button()

        if (button == 0 && rect.isCollided(mouseX, mouseY)) {
            if (isShowChoices) {
                var offsetY = 10f
                for (mode in list) {
                    if (rect.isCollided(mouseX, mouseY, rect.lt.x, rect.lt.y + 5 - mc.font.lineHeight / 2 + offsetY, rect.size.x, 10f)) {
                        value = mode
                    }
                    offsetY += 10f
                }
            }
            isShowChoices = !isShowChoices
            return true
        }

        return false
    }

    open class SubMode(val name: String, val parent: ChoiceValue) {
        init {
            parent.list.add(this)
        }

        operator fun getValue(thisRef: Any?, property: KProperty<*>) = selected()
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) { if (value) select() }

        fun selected() = this == parent.value

        fun select() = apply { parent.value = this }
    }
}