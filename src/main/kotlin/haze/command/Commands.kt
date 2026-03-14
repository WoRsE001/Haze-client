package haze.command

import haze.command.impl.*
import haze.event.Event
import haze.event.EventListener
import haze.event.impl.ChatMessageEvent
import haze.utility.nullCheck

// created by dicves_recode on 23.12.2025
@Suppress("UNUSED_EXPRESSION")
object Commands : EventListener {
    private val mutableList = mutableListOf<Command>()

    val list: List<Command>
        get() = mutableList

    internal fun add(command: Command) {
        mutableList += command
    }

    init {
        registerToEvents()

        CommandHelp
        CommandClip
        CommandBind
    }

    private var prefix = "."

    override fun onEvent(event: Event) {
        if (event is ChatMessageEvent.SEND) {
            val content = event.content

            if (!content.startsWith(prefix))
                return

            val args = content.removePrefix(prefix).split(" ")

            for (command in list) {
                if (args[0] !in command.aliases)
                    continue

                command.execute(args.drop(1))
                event.cancel()
                break
            }
        }
    }

    override fun shouldListenEvents() = nullCheck()
}
