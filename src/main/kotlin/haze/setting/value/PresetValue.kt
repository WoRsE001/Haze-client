package haze.setting.value

import haze.utility.math.Rect
import kotlinx.coroutines.Runnable
import kotlinx.serialization.json.*
import net.minecraft.client.gui.GuiGraphics
import kotlin.collections.first

// Blood! It's everywhere. SCWxD killed you on 14.02.2026 at 10:31.
class PresetValue(
    name: String,
    val listValues: List<Pair<String, Runnable>>,
    defaultValue: Pair<String, Runnable> = listValues[0]
) : Value<Pair<String, Runnable>>(name, defaultValue) {
    override var rect = Rect(0f, 0f, 0f, 0f)

    override var json: JsonObject
        get() = buildJsonObject {
            put("Preset", value.first)
        }
        set(value) {
            val name = value["Preset"]?.jsonPrimitive?.contentOrNull ?: return
            this.value = this.listValues.first { it.first == name }
            this.value.second.run()
        }

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
    }
}