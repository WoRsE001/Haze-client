package haze.config

import haze.HazeClient
import haze.config.Config.Companion.readFile
import haze.module.Modules
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import java.io.Closeable
import java.io.File
import java.io.FileWriter

// created by dicves_recode on 28.12.2025
object Binds : Closeable {
    private val DIR = File(HazeClient.DIR, "binds").apply { mkdirs() }
    private val FILE = File(DIR, "binds.json")

    init {
        load()
    }

    fun save() {
        FILE.createNewFile()

        val mainObject = buildJsonObject {
            put("Modules", Modules.keyJson)
        }

        val writer = FileWriter(FILE)
        writer.write(mainObject.toString())
        writer.close()
    }

    fun load() {
        if (FILE.createNewFile()) {
            HazeClient.info("При загрузке биндов файл не существовал, бинды сохранены")
            save()
            return
        }

        val mainObject = readFile(FILE)
        Modules.keyJson = mainObject["Modules"]?.jsonObject ?: run {
            HazeClient.error("При загрузке биндов не был найден блок \"Modules\"")
            return
        }
    }

    override fun close() {
        save()
    }
}
