package haze.key

// created by dicves_recode on 28.11.2025
interface KeyListener {
    var keybind: Keybind

    fun onKey(action: Int)
    fun shouldListenKeys(): Boolean

    fun registerToKeybinds() = KeyCaller.register(this)
}