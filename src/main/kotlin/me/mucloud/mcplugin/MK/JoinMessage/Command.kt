package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object CommandManager: CommandExecutor{

    override fun onCommand(sender: CommandSender, cmd: Command, s: String, ss: Array<out String>): Boolean {
        if(cmd.name.equals("mkjm", true)){

        }
        return false
    }

}

/**
 * ### /mkjm info
 *
 * | 显示插件版本和指令信息
 *
 */
fun CMD_INFO(sender: CommandSender){

}

/**
 * ### /mkjm list
 *
 * | 列出当前所有组
 *
 */
fun CMD_LIST(sender: CommandSender){

}

fun CMD_ADD_GROUP(sender: CommandSender, args: Array<String>){

}

fun CMD_DEL_GROUP(sender: CommandSender, args: Array<String>){

}

fun CMD_VANISH(sender: CommandSender, args: Array<String>){

}

fun CMD_ADD(sender: CommandSender, args: Array<String>){

}

fun CMD_DEL(sender: CommandSender, args: Array<String>){

}

fun CMD_SET(sender: CommandSender, args: Array<String>){

}

fun CMD_SETJM(sender: CommandSender, args: Array<String>){

}

fun CMD_SETQM(sender: CommandSender, args: Array<String>){

}

fun CMD_SETMODE(sender: CommandSender, args: Array<String>){

}

fun CMD_FIND(sender: CommandSender, args: Array<String>){

}

fun CMD_INFGROUP(sender: CommandSender, args: Array<String>){

}

