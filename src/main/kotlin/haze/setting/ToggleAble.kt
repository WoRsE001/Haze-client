package haze.setting

// created by dicves_recode on 30.11.2025
interface ToggleAble {
    var toggled: Boolean

    fun toggle() {
        toggled = !toggled
    }

    fun onEnable() {}
    fun onDisable() {}
}