package haze.config

import haze.HazeClient
import haze.module.Modules
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.put
import java.io.File
import java.io.FileWriter

// created by dicves_recode on 16.12.2025
class Config private constructor(
    val name: String,
    private val configFile: File,
    private var lastUpdateTimeStamp: Long
) {
    init {
        if (configFile.createNewFile())
            save()
    }

    fun save() {
        configFile.createNewFile()
        lastUpdateTimeStamp = System.currentTimeMillis()

        val mainObject = buildJsonObject {
            put("ConfigInfo", generateConfigInfo())
            put("Modules", Modules.json)
        }

        val writer = FileWriter(configFile)
        writer.write(mainObject.toString())
        writer.close()
    }

    private fun generateConfigInfo() = buildJsonObject {
        put("name", name)
        put("lastUpdateTimeStamp", lastUpdateTimeStamp)
    }

    fun load() {
        if (configFile.createNewFile()) {
            HazeClient.info("При загрузке конфига \"$name\" файла \"${configFile.name}\" несуществовало, конфиг сохранен в файл")
            save()
            return
        }

        val mainObject = readFile(configFile)
        Modules.json = mainObject["Modules"]?.jsonObject ?: run {
            HazeClient.error("При загрузке конфига \"$name\" не был найден блок \"Modules\"")
            return
        }
    }

    fun delete() {
        configFile.delete()
        Configs.deleteConfig(this)
    }

    override fun toString() = name

    companion object {
        const val FILE_EXTENSION = ".json"

        fun createFromFile(configFile: File): Config? {
            val mainObject = readFile(configFile)
            val configInfo = mainObject["ConfigInfo"]?.jsonObject ?: return null

            val configName = configInfo["name"]?.jsonPrimitive?.contentOrNull ?: return null
            val configLastUpdateTimeStamp = configInfo["lastUpdateTimeStamp"]?.jsonPrimitive?.longOrNull ?: return null

            return Config(configName, configFile, configLastUpdateTimeStamp)
        }

        fun create(name: String): Config {
            return Config(
                name,
                File(Configs.DIR, "$name$FILE_EXTENSION"),
                System.currentTimeMillis()
            )
        }

        fun readFile(file: File) = Json.parseToJsonElement(file.readText()).jsonObject
    }
}
