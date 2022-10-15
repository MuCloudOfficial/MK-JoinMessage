package me.mucloud.plugins.MK.JoinMessage

import org.bukkit.configuration.file.YamlConfiguration
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.IOException

object Configuration {

    private val ConfigFolder: File = Main.dataFolder
    private val ConfigFile: File = File(ConfigFolder, "config.yml")

    private var JoinMessage: String? = null
    private var ExitMessage: String? = null
    private var PermissionMessageMap: Map<String,Map.Entry<String, String>>? = null

    fun process(){
        checkIntegrity()
        loadConfig()
    }

    private fun checkIntegrity(){
        if(!ConfigFolder.exists()){
            Main.dataFolder.mkdir()
        }
        if(!ConfigFile.exists()){
            Main.saveDefaultConfig()
        }
    }

    private fun loadConfig(){
        val y = Yaml()
        var Pmap: HashMap<Any?, Any?> = y.loadAs(Main.getResource("plugin.yml"), HashMap.class)

    }


}