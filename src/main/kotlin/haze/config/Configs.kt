package haze.config

import haze.HazeClient
import java.io.Closeable
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList

// created by dicves_recode on 16.12.2025
object Configs : Closeable {
    val DIR = File(HazeClient.DIR, "configs").apply { mkdirs() }
    val DEFAULT = Config.create("default")

    val list = CopyOnWriteArrayList<Config>()

    init {
        try {
            DEFAULT.load()
            refresh()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun createConfig(name: String) {
        list.add(Config.create(name))
    }

    fun deleteConfig(config: Config) {
        list.remove(config)
    }

    fun refresh() {
        list.clear()

        DIR.listFiles().forEach {
            val config = Config.createFromFile(it) ?: return@forEach
            list.add(config)
        }
    }

    override fun close() {
        DEFAULT.save()
    }
}
