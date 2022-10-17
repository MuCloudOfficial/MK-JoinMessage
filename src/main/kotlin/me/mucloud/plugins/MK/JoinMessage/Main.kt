package me.mucloud.plugins.MK.JoinMessage

import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

object Main: JavaPlugin(){

    private const val Prefix = "§b§lMK§7§l-§e§lJoinMessage"

    override fun onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(Listeners, this)
        } else {
            throw RuntimeException("未检测到 PlaceholderAPI ,这将导致插件加载错误")
        }
        server.consoleSender.sendMessage("§7§l[" + getPrefix() + "§7§l] §a§l正在启动")
        getCommand("mkjm")!!.setExecutor(Commands)
        server.pluginManager.registerEvents(Listeners, this)
        Configuration.process()
    }

    override fun onDisable() {
        server.consoleSender.sendMessage("§7§l[" + getPrefix() + "§7§l] §a§l正在关闭")
        getCommand("mkjm")!!.setExecutor(null)
        HandlerList.unregisterAll(this)
        Configuration.clearConfig()
    }

    fun onReload() {
        Configuration.clearConfig()
        Configuration.process()
        server.consoleSender.sendMessage("§7§l[" + getPrefix() + "§7§l] §a§l重载完毕")
    }

    fun getPrefix(): String {
        return Prefix
    }

    fun getConfiguration(): IConfiguration {
        return Configuration
    }


}