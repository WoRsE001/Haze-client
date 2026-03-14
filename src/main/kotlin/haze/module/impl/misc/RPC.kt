package haze.module.impl.misc

import haze.module.Category
import haze.module.Module
import haze.utility.mc
import club.minnced.discord.rpc.DiscordEventHandlers
import club.minnced.discord.rpc.DiscordRPC
import club.minnced.discord.rpc.DiscordRichPresence
import kotlin.concurrent.thread

// Blood! It's everywhere. SCWxD killed you on 10.03.2026 at 14:32.
object RPC : Module("DiscordRPC", Category.MISC) {
    private val textInSinglePlayer by string("Text in a single player", "I'm playing in a single world: %NAME_WORLD")
    private val textInMultiPlayer by string("Text in a multi player", "I'm playing on the server: %NAME_SERVER")
    private val textInMenu by string("Text in a menu", "I'll start playing soon")

    val discordRPC = DiscordRPC.INSTANCE
    val handlers = DiscordEventHandlers()
    val discordAppId = "1480886291393413131"

    override fun onEnable() {
        discordRPC.Discord_Initialize(discordAppId, handlers, true, "")

        val presence = DiscordRichPresence()
        presence.startTimestamp = System.currentTimeMillis() / 1000

        thread(start = true, isDaemon = true) {
            while (!Thread.currentThread().isInterrupted) {
                if (mc.isSingleplayer) {
                    presence.details = textInSinglePlayer.replace("%NAME_WORLD", mc.singleplayerServer?.motd ?: "")
                } else if (mc.currentServer != null) {
                    presence.details = textInMultiPlayer.replace("%NAME_SERVER", mc.currentServer?.ip ?: "")
                } else {
                    presence.details = textInMenu
                }

                discordRPC.Discord_UpdatePresence(presence)
                discordRPC.Discord_RunCallbacks()

                try {
                    Thread.sleep(2000)
                } catch (_: InterruptedException) {
                    break
                }
            }
        }
    }

    override fun onDisable() {
        discordRPC.Discord_Shutdown()
    }
}