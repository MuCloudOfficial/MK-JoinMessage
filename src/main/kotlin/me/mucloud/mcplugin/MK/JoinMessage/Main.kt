package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {

    override fun onEnable(){
        Configuration.preInit(this)
        MessageSender.init(this)
        checkPapiHook()
        Configuration.init()
        GroupManager.init()
        CommandManager.init(this)
        Listener.init(this)
        Updater.init(this)
        MessageSender.sendToConsole(MessageLevel.FINISH, "已成功加载 ${Prefix(false)}")
    }

    override fun onDisable(){
        Updater.unInit()
        Listener.unInit()
        CommandManager.unInit()
        GroupManager.unInit()
        Configuration.unInit()
        MessageSender.sendToConsole(MessageLevel.FINISH, "已成功卸载 ${Prefix(false)}")
    }

    private fun checkPapiHook(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            PAPI_HOOK = true
            MessageSender.sendToConsole(MessageLevel.FINISH, "已检测到 PlaceholderAPI, 启动 PAPI 附加功能")
        }else{
            MessageSender.sendToConsole(MessageLevel.WARN, "没有检测到 PlaceholderAPI, 已忽略")
        }
    }

    companion object{

        private var PAPI_HOOK = false

        internal fun Prefix(useLog: Boolean): String =
            if(useLog){
                "MK-JoinMessage"
            }else{
                "§bMK§7-§6JoinMessage§f"
            }

        internal fun isPAPIHook(): Boolean = PAPI_HOOK
    }

}