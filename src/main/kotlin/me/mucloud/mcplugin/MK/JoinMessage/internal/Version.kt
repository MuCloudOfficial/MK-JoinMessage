package me.mucloud.mcplugin.MK.JoinMessage.internal

import me.mucloud.mcplugin.MK.JoinMessage.Main
import org.yaml.snakeyaml.Yaml

class Version(private val main: Main) {

    private lateinit var Version: String
    private lateinit var VersionCN: String
    private lateinit var VersionView: String
    private lateinit var VersionCNView: String
    private lateinit var AuthorList: List<String>
    private var dev: Int = 0

    init {
        getCurrentVersion()

        MessageSender.sendToConsole(MessageLevel.INFO, "MK-JoinMessage | ${Version} | $VersionCN | DEV.$dev")
        MessageSender.sendToConsole(MessageLevel.INFO, "作者：${getAuthorList()}")
        MessageSender.sendToConsole(MessageLevel.INFO, "MADE IN SAKURA OCEAN & BASED ON SPIGOT API")
    }

    fun getCurrentVersion(){
        Yaml().load<Map<String, Any>>(main.getResource("plugin.yml")).also {
            Version = it["version"] as String
            VersionCN = it["versionCN"] as String
            VersionView = it["versionV"] as String
            VersionCNView = it["versionCNV"] as String
            AuthorList = it["authors"] as List<String>
            dev = it["internalVersion"] as Int
        }
    }

    fun getVersion(): String = Version
    fun getVersionCN(): String = VersionCN
    fun getAuthorList(): List<String> = AuthorList
    fun getDev(): Int = dev
    fun getDevView(): String = VersionCNView
    fun getDevViewCN(): String = VersionCNView

}