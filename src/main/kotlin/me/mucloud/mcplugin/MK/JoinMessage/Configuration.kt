package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.configuration.file.YamlConfiguration
import java.io.InputStreamReader

object Configuration {

    private lateinit var Version: String
    private lateinit var VersionCN: String
    private lateinit var VersionView: String
    private lateinit var VersionCNView: String
    private lateinit var VersionType: String

    private val ConfigPool: Map<String, Any> = mutableMapOf(
        Pair("conf.version", 1),
        Pair("db.always_connect", false)
    )

    private fun getVersion() = YamlConfiguration().also{
        it.load(InputStreamReader(Main.Companion::class.java.getResourceAsStream("plugin.yml")!!, "utf-8"))
        Version = it.getString("version")!!
        VersionCN = it.getString("versionCN")!!
        VersionView = it.getString("versionV")!!
        VersionCNView = it.getString("versionCNV")!!
        VersionType = it.getString("verType")!!
    }

    fun init(){
        SQLITEConnector.getConf()
    }

}

class Updater(main: Main){

    private val REMOTE_URL = "https://gitee.com/MuCloudOfficial/MK-JoinMessage/raw/Kotlin-dev/build.gradle.kts"

    init{
        suspend{



        }
    }


}