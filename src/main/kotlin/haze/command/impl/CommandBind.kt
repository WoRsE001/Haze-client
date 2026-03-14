package haze.command.impl

import haze.command.Command
import haze.module.Modules
import haze.utility.inputByName
import haze.utility.mc
import haze.utility.sendMessage

// created by dicves_recode on 23.12.2025
object CommandBind : Command(
    "Bind",
    "Bind modules",
    ".b/bind <module> <key>",
    "b", "bind"
) {
    override fun execute(args: List<String>) {
        if (args.size !in 2..3) {
            mc.sendMessage(usage)
            return
        }

        val module = Modules.getModule(args[0])
        requireNotNull(module) { mc.sendMessage("Module ${args[0]} dont exists"); return }

        val key = inputByName(args[1].uppercase())
        module.keybind.key = key.value

        if (args.size > 2)
            module.keybind.hold = args[2] == "true" || args[2] == "1"

        mc.sendMessage("$module bound to ${key.displayName} (hold=${module.keybind.hold})")
    }
}
