package haze.event

// created by dicves_recode on 28.11.2025
open class CancelAbleEvent : Event {
    var canceled = false
        private set

    fun cancel() {
        canceled = true
    }

    internal fun reset() {
        canceled = false
    }
}
