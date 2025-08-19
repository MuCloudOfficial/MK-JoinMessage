package me.mucloud.mcplugin.MK.JoinMessage

import me.mucloud.mcplugin.MK.JoinMessage.group.GroupManager
import me.mucloud.mcplugin.MK.JoinMessage.hook.PAPIHooker
import me.mucloud.mcplugin.MK.JoinMessage.internal.CommandManager
import me.mucloud.mcplugin.MK.JoinMessage.internal.Configuration
import me.mucloud.mcplugin.MK.JoinMessage.internal.Listener
import me.mucloud.mcplugin.MK.JoinMessage.internal.Logger
import me.mucloud.mcplugin.MK.JoinMessage.internal.MessageLevel
import me.mucloud.mcplugin.MK.JoinMessage.internal.MessageSender
import me.mucloud.mcplugin.MK.JoinMessage.internal.Version
import net.md_5.bungee.api.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {

    override fun onEnable(){
        Logger = Logger(this)
        Version = Version(this)
        Configuration.preInit(this)
        PAPIHooker.init()
        Configuration.init()
        GroupManager.init()
        CommandManager.init(this)
        Listener.init(this)
        MessageSender.sendToConsole(MessageLevel.FINISH, "已成功加载 ${Prefix(false)}")
    }

    override fun onDisable(){
        Listener.unInit()
        CommandManager.unInit()
        GroupManager.save()
        Configuration.unInit()
        MessageSender.sendToConsole(MessageLevel.FINISH, "已成功卸载 ${Prefix(false)}")
    }

    companion object{

        private lateinit var Logger: Logger
        private lateinit var Version: Version

        fun Prefix(fancy: Boolean): String =
            if (fancy) "${ChatColor.AQUA}${ ChatColor.BOLD}MK${ChatColor.GRAY}-${ChatColor.YELLOW}JoinMessage${ChatColor.RESET}" else "MK-JoinMessage"

        fun getLogger() = Logger
        fun getVersion() = Version
    }

}