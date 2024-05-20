package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.ChatColor
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class MessageSender(main: Main) {

    init{
        INSTANCE = main.server.consoleSender
    }

    companion object{

        private lateinit var INSTANCE: ConsoleCommandSender

        fun sendMessageToConsole(lvl: MessageLevel, msg: String){
            INSTANCE.sendMessage("${Main.getPrefix(true)}$lvl$msg")
        }

        fun sendMessage(lvl: MessageLevel, target: Player, msg: String){
            target.sendMessage("${Main.getPrefix(false)}$lvl$msg")
        }

        fun sendBossBarMessage(lvl: MessageLevel, target: Player, msg: String){

        }

        fun sendBossBarMessage(lvl: MessageLevel, msg: String){

        }

        fun sendActionBarMessage(lvl: MessageLevel, target: Player, msg: String){

        }

        fun sendActionBarMessage(lvl: MessageLevel, msg: String){

        }

    }

}

enum class MessageLevel(private val color: ChatColor){

    INFO(ChatColor.WHITE),
    FINISH(ChatColor.GREEN),
    WARN(ChatColor.GOLD),
    ERR(ChatColor.RED);

    override fun toString(): String = color.toString()


}