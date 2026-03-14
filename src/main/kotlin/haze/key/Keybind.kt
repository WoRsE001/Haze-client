package haze.key

import haze.setting.SaveLoadAble
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

// created by dicves_recode on 28.11.2025
data class Keybind(var key: Int, var hold: Boolean = false) : SaveLoadAble {
    override var json: JsonObject
        get() = buildJsonObject {
            put("Key", key)
            put("Hold", hold)
        }
        set(value) {
            key = value["Key"]?.jsonPrimitive?.intOrNull ?: return
            hold = value["Hold"]?.jsonPrimitive?.booleanOrNull ?: return
        }

    companion object {
        val NONE = Keybind(-1)
    }
}
