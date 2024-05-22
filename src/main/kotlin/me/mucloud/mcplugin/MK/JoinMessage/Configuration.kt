package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.configuration.file.YamlConfiguration
import java.io.InputStreamReader
import java.util.*

object Configuration {

    private lateinit var Version: String
    private lateinit var VersionCN: String
    private lateinit var VersionView: String
    private lateinit var VersionCNView: String

    private lateinit var VersionType: String

    private fun getVersion() = YamlConfiguration().also{
        it.load(InputStreamReader(Main.Companion::class.java.getResourceAsStream("plugin.yml")!!, "utf-8"))
        Version = it.getString("version")!!
        VersionCN = it.getString("versionCN")!!
        VersionView = it.getString("versionV")!!
        VersionCNView = it.getString("versionCNV")!!
        VersionType = it.getString("verType")!!
    }



}