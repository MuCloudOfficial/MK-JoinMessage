package me.mucloud.mcplugin.MK.JoinMessage.internal

import me.clip.placeholderapi.PlaceholderAPI
import me.mucloud.mcplugin.MK.JoinMessage.Main
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.io.File
import java.io.FileWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat

object MessageSender {

    private val CCS: ConsoleCommandSender = Bukkit.getConsoleSender()
    private var ENABLE_LOGGER = false
    private lateinit var LOGFILE: File

    internal fun init(main: Main, toLog: Boolean = false){
        ENABLE_LOGGER = toLog
        LOGFILE = File(main.dataFolder, "mkjm.log")
        enableLogger()
    }

    private fun enableLogger(){
        if(ENABLE_LOGGER){
            return
        }
        ENABLE_LOGGER = true
        if(!LOGFILE.exists()){
            LOGFILE.createNewFile()
        }
        toLog(MessageLevel.FINISH, "File logger created.")
    }

    fun closeLogger(){
        if(!ENABLE_LOGGER){
            return
        }
        ENABLE_LOGGER = false
        toLog(MessageLevel.FINISH, "File logger closed.")
    }

    fun convert(request: Player, msg: String): String{
        val colorConverted = ChatColor.translateAlternateColorCodes('&', msg)
        return if(P()){
            PlaceholderAPI.setPlaceholders(request, colorConverted.replace("{player}", request.name))
        }else{
            colorConverted.replace("{player}", request.name)
        }
    }

    internal fun sendMessage(lvl: MessageLevel, target: CommandSender, msg: String){
        target.sendMessage("$lvl$msg")
        toLog(lvl, "[Plugin -> CommandSender(${target.name})] $msg")
    }

    internal fun sendToConsole(lvl: MessageLevel, msg: String){
        CCS.sendMessage("$lvl$msg")
        toLog(lvl, "[Plugin -> Console]: $msg")
    }

    internal fun sendMessage(lvl: MessageLevel, player: Player, msg: String){
        player.sendMessage("$lvl$msg")
        toLog(lvl, "[Plugin -> Player(${player.name})] $msg")
    }

    internal fun sendMessage(lvl: MessageLevel, playerList: List<OfflinePlayer>, msg: String){
        playerList.forEach {
            if(it.isOnline){
                sendMessage(lvl, it as Player, msg)
            }
        }
    }

    private fun toLog(lvl: MessageLevel, msg: String){
        if(!ENABLE_LOGGER){
            return
        }
        when(lvl){
            MessageLevel.NULL,
            MessageLevel.FINISH,
            MessageLevel.INFO -> {
                writeLog(MessageLevel.INFO, msg)
            }
            MessageLevel.WARN -> {
                writeLog(MessageLevel.WARN, msg)
            }
            MessageLevel.ERR -> {
                writeLog(MessageLevel.ERR, msg)
            }
        }
    }

    private fun writeLog(lvl: MessageLevel, msg: String){
        if(!ENABLE_LOGGER){
            return
        }

        val fw = FileWriter(LOGFILE, StandardCharsets.UTF_8, true)
        fw.write("[${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").calendar.time}][${lvl.name}] $msg\n")
        fw.flush()
        fw.close()
    }

}

internal enum class MessageLevel(private val fc: String){
    NULL("§f "),
    INFO("§7| "),
    FINISH("§7| §a"),
    WARN("§7| §6"),
    ERR("§7| §4")
    ;

    override fun toString(): String = fc

}