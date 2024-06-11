package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.ChatColor
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MessageSender(main: Main) {

    init{
        INSTANCE = main.server.consoleSender
    }

    companion object{

        private lateinit var INSTANCE: ConsoleCommandSender
        private val LOGGER: Logger = LoggerFactory.getLogger(Main.getPrefix2Logger())

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

        fun logInfo(msg: String) = LOGGER.info(msg)

        fun logWarn(msg: String, warn: Throwable) = LOGGER.warn(msg, warn)

        fun logErr(msg: String, err: Throwable) = LOGGER.error(msg, err)

    }

}

enum class MessageLevel(private val color: ChatColor){

    INFO(ChatColor.WHITE),
    FINISH(ChatColor.GREEN),
    WARN(ChatColor.GOLD),
    ERR(ChatColor.RED);

    override fun toString(): String = color.toString()


}