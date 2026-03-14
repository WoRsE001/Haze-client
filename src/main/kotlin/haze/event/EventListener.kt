package haze.event

// created by dicves_recode on 28.11.2025
interface EventListener {
    fun onEvent(event: Event) {}
    fun shouldListenEvents(): Boolean

    fun registerToEvents() = EventCaller.register(this)
}
