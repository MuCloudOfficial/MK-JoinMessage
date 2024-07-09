package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {

    private var PAPI_HOOK = false

    override fun onEnable(){
        MessageSender.init(this)
        Configuration.preInit(this)
        SQLITEConnector.init(this)
        Configuration.init()
        GroupManager.init()
        regCommand()
    }

    override fun onDisable(){
        SQLITEConnector.flushAll()
    }

    private fun regCommand() = getCommand("mkjm")?.setExecutor(CommandManager)

    private fun checkPapiHook(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            PAPI_HOOK = true
        }
    }

    companion object{
        internal fun Prefix(useLog: Boolean): String =
            if(useLog){
                "MK-JoinMessage"
            }else{
                "§bMK§7-§6JoinMessage§f"
            }
    }

}