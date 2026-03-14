package haze.setting.value

import haze.utility.math.Rect
import kotlinx.serialization.json.*
import net.minecraft.client.gui.GuiGraphics

// Blood! It's everywhere. SCWxD killed you on 13.03.2026 at 7:51.
class StringValue(name: String, defaultValue: String) : Value<String>(name, defaultValue) {
    override var json: JsonObject
        get() = buildJsonObject {
            put("String", value)
        }
        set(value) {
            this.value = value["String"]?.jsonPrimitive?.contentOrNull ?: return
        }

    override var rect = Rect(0f, 0f, 0f, 0f)

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) { }
}