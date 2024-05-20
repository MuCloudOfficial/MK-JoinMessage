package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.nio.charset.StandardCharsets

object Configuration {

    private lateinit var ConfigFolder: File
    private val ConfigFile: File = File(ConfigFolder, "config.yml")

    private lateinit var PluginDFReader: YamlConfiguration
    private lateinit var ConfigReader: YamlConfiguration

    private lateinit var ConfigVersion: String
    private lateinit var SendMode: SendMode

    private lateinit var PluginVersion: String

    fun init(main: Main){
        ConfigFolder = main.dataFolder

        PluginDFReader = YamlConfiguration.loadConfiguration(main.getResource("plugin.yml")!!.reader(StandardCharsets.UTF_8))
        ConfigReader = YamlConfiguration.loadConfiguration(ConfigFile)
    }

    fun readConfig(){
        ConfigReader.getConfigurationSection("Groups")?.getValues(true)
    }



}