package me.mucloud.plugins.MK.JoinMessage

import org.bukkit.plugin.java.JavaPlugin

object Main: JavaPlugin(){

    private const val Prefix = "§b§lMK§7§l-§e§lJoinMessage"

    // 加载 Configuration
    override fun onLoad() {
        Configuration.process()
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }

    fun getPrefix(): String{
        return Prefix
    }

}