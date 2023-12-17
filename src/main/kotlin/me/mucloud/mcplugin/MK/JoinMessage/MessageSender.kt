package me.mucloud.mcplugin.MK.JoinMessage

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.ChatColor
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

val converters: List<(String, Player) -> String> = listOf(
    fun(s: String, c: Player): String{
        return s.replace("{player}", c.name)
    }
)

fun convertMessage(msg: String): String{
    return ChatColor.translateAlternateColorCodes('&', msg)
}

fun convertMessage(msg: String, caller: Player): String{
    return if(PlaceholderAPIHooker.isHook()){
        PlaceholderAPI.setPlaceholders(caller, convertMessage(msg))
    }else{
        var s: String = msg
        converters.forEach { i ->
            s = i(msg, caller)
        }

        s
    }
}

object ConsoleSender{

    private lateinit var main: ConsoleCommandSender

    fun init(main: ConsoleCommandSender){
        this.main = main
    }

    fun info(msg: String){
        main.sendMessage("${PREFIX} §7§l| $msg")
    }

    fun warn(msg: String){
        main.sendMessage("${PREFIX} §6§l| $msg")
    }

    fun err(msg: String){
        main.sendMessage("${PREFIX} §4§l| $msg")
    }

}