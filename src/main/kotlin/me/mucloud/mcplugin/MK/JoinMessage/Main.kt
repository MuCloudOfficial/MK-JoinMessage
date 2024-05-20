package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {

    override fun onEnable() {
        MessageSender(this)


    }

    override fun onDisable() {
        
    }

    internal fun regCommand() = getCommand("mkjm")?.setExecutor(CommandManager)
    
    companion object{
        
        private val PREFIX: String = "§e§lMK§7§l-§b§lJoinMessage"
        
        fun getPrefix(useToLog: Boolean): String{
            return if(useToLog){
                "§7§l[$PREFIX§7§l]§f "
            }else{
                "$PREFIX §b§l>>>§f "
            }
        }
        
    }

}