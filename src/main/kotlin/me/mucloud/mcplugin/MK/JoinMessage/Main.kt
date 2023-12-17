package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

val PREFIX: String = "§b§lMK§7§l-§e§lJoinMessage"

class Main: JavaPlugin() {

    override fun onEnable() {
        ConsoleSender.init(server.consoleSender)

        ConfigurationSerialization.registerClass(Group::class.java)
        Configuration.init(this)

        getCommand("mkjm")!!.setExecutor(CommandManager)

        server.pluginManager.registerEvents(Listener, this)
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)
    }

    fun onReload() {

    }

}
