package haze.event

// created by dicves_recode on 28.11.2025
interface Event {
    fun call() = EventCaller.call(this)
}
