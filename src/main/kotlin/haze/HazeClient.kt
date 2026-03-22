package haze

import haze.command.Commands
import haze.config.Binds
import haze.config.Configs
import haze.event.EventCaller
import haze.key.KeyCaller
import haze.module.Modules
import haze.utility.mc
import haze.utility.player.Clicker
import haze.utility.rotation.PlayerDeltaTracker
import haze.utility.rotation.RotationHandler
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.io.File
import kotlin.system.measureTimeMillis

@Suppress("UNUSED_EXPRESSION")
object HazeClient : ClientModInitializer, Closeable {
	const val NAME = "Haze"
	const val MOD_ID = "haze-client"

	val DIR = File(mc.gameDirectory, MOD_ID).apply { mkdirs() }
	val logger: Logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitializeClient() {
		val startTime = measureTimeMillis {
			EventCaller
			KeyCaller
			RotationHandler

			PlayerDeltaTracker

			Modules
			Configs
			Binds
			Commands

			Clicker
		}

		info("Клиент запущен за $startTime мс.")
	}

	override fun close() {
		Configs.close()
		Binds.close()
	}

	fun info(info: String) = logger.info(info)
	fun warn(warn: String) = logger.warn(warn)
	fun error(error: String) = logger.error(error)

	fun of(path: String) = Identifier.fromNamespaceAndPath(MOD_ID, path)
}