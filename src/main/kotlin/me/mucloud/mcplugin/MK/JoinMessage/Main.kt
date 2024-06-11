package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {

    override fun onEnable(){
        MessageSender(this)
        SQLITEConnector.init(this)
        Configuration.
        GroupManager.init()

    }

    override fun onDisable(){
        
    }

    private fun regCommand() = getCommand("mkjm")?.setExecutor(CommandManager)
    
    companion object{
        
        private const val PREFIX: String = "§e§lMK§7§l-§b§lJoinMessage"

        
        fun getPrefix(useToLog: Boolean): String =
            if(useToLog){
                "§7§l[$PREFIX§7§l]§f "
            }else{
                "$PREFIX §b§l>>>§f "
            }

        fun getPrefix2Logger(): String = PREFIX
        
    }

}