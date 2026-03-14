package haze.setting.value

import haze.gui.RenderAble
import haze.setting.SaveLoadAble
import kotlin.reflect.KProperty

// испорченно SCWGxD в 30.01.2026:23:50
abstract class Value<T>(val name: String, private val defaultValue: T) : SaveLoadAble, RenderAble {
    protected var value = defaultValue

    var visible: () -> Boolean = { true }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        get()

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) =
        set(value)

    open fun get() = value

    open fun set(value: T) {
        this.value = value
    }

    open fun reset() {
        value = defaultValue
    }

    fun visible(visible: () -> Boolean) = apply {
        this.visible = visible
    }
}