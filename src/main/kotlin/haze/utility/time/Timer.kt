package haze.utility.time

// испорченно SCWGxD в 14.12.2025:18:01
class Timer(
    private var lastTime: Long = System.currentTimeMillis()
) {
    var reached = 0L
        get() = System.currentTimeMillis() - lastTime
        set(value) { field = System.currentTimeMillis() - value }

    fun reset() {
        lastTime = System.currentTimeMillis()
    }
}