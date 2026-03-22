package haze.setting

import haze.setting.value.*
import haze.utility.math.Rect
import haze.utility.mc
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.MouseButtonEvent

// created by dicves_recode on 14.12.2025
open class ConfigureAble(
    name: String,
    value: MutableCollection<Value<*>> = mutableListOf(),
) : Value<MutableCollection<Value<*>>>(name, value) {
    override var rect = Rect(0f, 0f, 0f, 10f)

    var owner: ConfigureAble? = null
    var isShowSettings = false

    override var json: JsonObject
        get() = buildJsonObject {
            for (item in value) {
                put(item.name, item.json)
            }
        }
        set(value) {
            for (item in this.value) {
                item.json = value[item.name]?.jsonObject ?: continue
            }
        }

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        rect.size.x = mc.font.width("$name +").toFloat()
        rect.size.y = 10f
        val nameModificator = if (isShowSettings) "-" else "+"
        guiGraphics.drawString(mc.font, "$name $nameModificator", rect.lc.x.toInt(), (rect.lc.y - mc.font.lineHeight / 2).toInt(), -1, false)

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

        if (button == 0 && value.isNotEmpty() && rect.isCollided(mouseX, mouseY, rect.lt.x, rect.lt.y, rect.size.x, 10f)) {
            isShowSettings = !isShowSettings
            return true
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

    override fun reset() {
        value.forEach { it.reset() }
    }

    fun isEmpty() = value.isEmpty()
    fun isNotEmpty() = value.isNotEmpty()

    fun <T : ConfigureAble> tree(configureAble: T): T {
        value.add(configureAble)
        configureAble.owner = this
        return configureAble
    }

    fun boolean(
        name: String,
        default: Boolean
    ) = BooleanValue(name, default).apply {
        this@ConfigureAble.value += this
    }

    fun list(
        name: String
    ) = ChoiceValue(name).apply {
        this@ConfigureAble.value += this
    }

    fun numberRange(
        name: String,
        default: ClosedFloatingPointRange<Double>,
        range: ClosedFloatingPointRange<Double>,
        step: Double,
        suffix: String = ""
    ) = NumberRangeValue(name, default, range, step, suffix).apply {
        this@ConfigureAble.value += this
    }

    fun number(
        name: String,
        default: Double,
        range: ClosedFloatingPointRange<Double>,
        step: Double,
        suffix: String = ""
    ) = NumberValue(name, default, range, step, suffix).apply {
        this@ConfigureAble.value += this
    }

    fun string(
        name: String,
        default: String
    ) = StringValue(name, default).apply {
        this@ConfigureAble.value += this
    }

    fun group(
        name: String
    ) = ConfigureAble(name, mutableListOf()).apply {
        this@ConfigureAble.value += this
    }

    fun toggleAbleGroup(
        name: String,
        state: Boolean
    ) = ToggleAbleConfigureAble(name, state).apply {
        this@ConfigureAble.value += this
    }
}
