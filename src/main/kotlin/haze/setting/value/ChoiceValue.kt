package haze.setting.value

import haze.setting.ConfigureAble
import haze.setting.value.ChoiceValue.Choice
import haze.utility.math.Rect
import haze.utility.mc
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.MouseButtonEvent

// испорченно SCWGxD в 31.01.2026:9:45
class ChoiceValue(
    name: String
) : Value<Choice?>(name, null) {
    private val _choices = mutableListOf<Choice>()
    private var isShowChoices = false

    val choices: List<Choice>
        get() = _choices

    fun choice(name: String) = Choice(name).apply {
        parent = this@ChoiceValue
        _choices += this
    }

    fun choice(choice: Choice) = choice.apply {
        parent = this@ChoiceValue
        _choices += this
    }

    override var rect = Rect(0f, 0f, 0f, 0f)

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        val nameModificator = if (isShowChoices) "-" else "+"
        val sizeX = choices.maxOfOrNull { mc.font.width(it.name) }?.plus(mc.font.width("$name:  $nameModificator"))
        rect.size.x = sizeX?.toFloat()!!
        rect.size.y = 10f * if (isShowChoices) choices.size + 1 else 1

        guiGraphics.fill(rect.lt.x.toInt(), rect.lt.y.toInt(), rect.rb.x.toInt(), rect.rb.y.toInt(), 0xff2d2d2d.toInt())
        guiGraphics.drawString(mc.font, "$name: ${value?.name}", rect.lt.x.toInt(), (rect.lt.y + 5 - mc.font.lineHeight / 2).toInt(), -1, false)
        guiGraphics.drawString(mc.font, nameModificator, (rect.rt.x - mc.font.width(nameModificator)).toInt(), (rect.rt.y + 5 - mc.font.lineHeight / 2).toInt(), -1, false)

        if (isShowChoices) {
            var offsetY = 10f
            for (mode in choices) {
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
                for (mode in choices) {
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

    override var json: JsonObject
        get() = buildJsonObject {
            put("choice", value?.name)
        }
        set(value) {
            val name = value["choice"]?.jsonPrimitive?.contentOrNull ?: return
            this.value = _choices.firstOrNull { it.name == name } ?: return
        }

    open class Choice internal constructor(
        name: String
    ) : ConfigureAble(name) {
        lateinit var parent: ChoiceValue

        fun select() = apply {
            parent.set(this)
        }

        fun selected() = parent.get() == this
    }
}
//class ChoiceValue(name: String, val parent: ConfigureAble) : Value<SubMode?>(name, null) {
//    override var rect = Rect(0f, 0f, 0f, 0f)
//
//    val choices = mutableListOf<SubMode>()
//    var isShowChoices = false
//
//    override var json: JsonObject
//        get() = buildJsonObject {
//            put("Choice", value?.name)
//        }
//        set(value) {
//            val name = value["Choice"]?.jsonPrimitive?.contentOrNull ?: return
//            this.value = choices.first { it.name == name }
//        }
//
//    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
//        val nameModificator = if (isShowChoices) "-" else "+"
//        val sizeX = choices.maxOfOrNull { mc.font.width(it.name) }?.plus(mc.font.width("$name:  $nameModificator"))
//        rect.size.x = sizeX?.toFloat()!!
//        rect.size.y = 10f * if (isShowChoices) choices.size + 1 else 1
//
//        guiGraphics.fill(rect.lt.x.toInt(), rect.lt.y.toInt(), rect.rb.x.toInt(), rect.rb.y.toInt(), 0xff2d2d2d.toInt())
//        guiGraphics.drawString(mc.font, "$name: ${value?.name}", rect.lt.x.toInt(), (rect.lt.y + 5 - mc.font.lineHeight / 2).toInt(), -1, false)
//        guiGraphics.drawString(mc.font, nameModificator, (rect.rt.x - mc.font.width(nameModificator)).toInt(), (rect.rt.y + 5 - mc.font.lineHeight / 2).toInt(), -1, false)
//
//        if (isShowChoices) {
//            var offsetY = 10f
//            for (mode in choices) {
//                guiGraphics.drawString(mc.font, mode.name, rect.lt.x.toInt(), (rect.lt.y + 5 - mc.font.lineHeight / 2 + offsetY).toInt(), -1, false)
//                offsetY += 10f
//            }
//        }
//    }
//
//    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
//        val mouseX = mouseButtonEvent.x.toFloat()
//        val mouseY = mouseButtonEvent.y.toFloat()
//        val button = mouseButtonEvent.button()
//
//        if (button == 0 && rect.isCollided(mouseX, mouseY)) {
//            if (isShowChoices) {
//                var offsetY = 10f
//                for (mode in choices) {
//                    if (rect.isCollided(mouseX, mouseY, rect.lt.x, rect.lt.y + 5 - mc.font.lineHeight / 2 + offsetY, rect.size.x, 10f)) {
//                        value = mode
//                    }
//                    offsetY += 10f
//                }
//            }
//            isShowChoices = !isShowChoices
//            return true
//        }
//
//        return false
//    }
//
//    fun subMode(name: String) = SubMode(name, this)
//
//    open class SubMode(val name: String, val parent: ChoiceValue) {
//        init {
//            parent.choices.add(this)
//        }
//
//        operator fun getValue(thisRef: Any?, property: KProperty<*>) = selected()
//        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) { if (value) select() }
//
//        fun selected() = this == parent.value
//
//        fun select() = apply { parent.value = this }
//
//        fun boolean(
//            name: String,
//            default: Boolean,
//            parent: ConfigureAble = this.parent.parent
//        ) = BooleanValue(name, default).apply {
//            parent.get().add(this)
//            visible { selected() }
//        }
//
//        fun list(
//            name: String,
//            parent: ConfigureAble = this.parent.parent
//        ) = ChoiceValue(name, parent).apply {
//            parent.get().add(this)
//            visible { selected() }
//        }
//
//        fun numberRange(
//            name: String,
//            default: ClosedFloatingPointRange<Double>,
//            range: ClosedFloatingPointRange<Double>,
//            step: Double,
//            suffix: String = "",
//            parent: ConfigureAble = this.parent.parent
//        ) = NumberRangeValue(name, default, range, step, suffix).apply {
//            parent.get().add(this)
//            visible { selected() }
//        }
//
//        fun number(
//            name: String,
//            default: Double,
//            range: ClosedFloatingPointRange<Double>,
//            step: Double,
//            suffix: String = "",
//            parent: ConfigureAble = this.parent.parent
//        ) = NumberValue(name, default, range, step, suffix).apply {
//            parent.get().add(this)
//            visible { selected() }
//        }
//
//        fun string(
//            name: String,
//            default: String,
//            parent: ConfigureAble = this.parent.parent
//        ) = StringValue(name, default).apply {
//            parent.get().add(this)
//            visible { selected() }
//        }
//
//        fun group(
//            name: String,
//            parent: ConfigureAble = this.parent.parent
//        ) = ConfigureAble(name, mutableListOf()).apply {
//            parent.get().add(this)
//            visible { selected() }
//        }
//
//        fun toggleAbleGroup(
//            name: String,
//            state: Boolean,
//            parent: ConfigureAble = this.parent.parent
//        ) = ToggleAbleConfigureAble(name, state).apply {
//            parent.get().add(this)
//            visible { selected() }
//        }
//    }
//}