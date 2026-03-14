package haze.command.impl

import haze.command.Command
import haze.command.Commands
import haze.utility.mc
import net.minecraft.network.chat.Component

// created by dicves_recode on 23.12.2025
object CommandHelp : Command(
    "Help",
    "",
    ".help",
    "help"
) {
    override fun execute(args: List<String>) {
        Commands.list.forEach {
            mc.gui.chat.addMessage(Component.literal(
                "${it.name}: ${it.desc}. usage: ${it.usage}"
            ))
        }
    }
}
