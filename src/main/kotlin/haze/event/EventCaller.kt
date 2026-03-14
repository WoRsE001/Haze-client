package haze.event

// created by dicves_recode on 28.11.2025
object EventCaller {
    private val listeners = mutableSetOf<EventListener>()

    internal fun register(listener: EventListener) {
        listeners += listener
    }

    fun call(event: Event) {
        if (event is CancelAbleEvent)
            event.reset()

        listeners.forEach { if (it.shouldListenEvents()) it.onEvent(event) }
    }
}
