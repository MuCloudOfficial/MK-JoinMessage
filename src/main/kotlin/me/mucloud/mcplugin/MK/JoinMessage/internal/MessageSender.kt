package me.mucloud.mcplugin.MK.JoinMessage.internal

import me.mucloud.mcplugin.MK.JoinMessage.Main
import me.mucloud.mcplugin.MK.JoinMessage.hook.PAPIHooker.convert
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

object MessageSender {

    private val CCS: ConsoleCommandSender = Bukkit.getConsoleSender()

    fun convert(request: Player, msg: String): String =
        msg.colored().convert(request)

    internal fun sendMessage(lvl: MessageLevel, target: CommandSender, msg: String){
        target.sendMessage("$lvl$msg")
        Main.getLogger().toLog(lvl, "[Plugin -> CommandSender(${target.name})] $msg")
    }

    internal fun sendToConsole(lvl: MessageLevel, msg: String){
        CCS.sendMessage("$lvl$msg")
        Main.getLogger().toLog(lvl, "[Plugin -> Console]: $msg")
    }

    internal fun sendMessage(lvl: MessageLevel, player: Player, msg: String){
        player.sendMessage("$lvl$msg")
        Main.getLogger().toLog(lvl, "[Plugin -> Player(${player.name})] $msg")
    }

    internal fun sendMessage(lvl: MessageLevel, playerList: List<OfflinePlayer>, msg: String){
        playerList.forEach {
            if(it.isOnline){
                sendMessage(lvl, it as Player, msg)
            }
        }
    }

    fun String.colored(): String = ChatColor.translateAlternateColorCodes('&', this)

}

enum class MessageLevel(private val fc: String){
    NULL("§f "),
    INFO("§7| "),
    FINISH("§7| §a"),
    WARN("§7| §6"),
    ERR("§7| §4")
    ;

    override fun toString(): String = fc

}