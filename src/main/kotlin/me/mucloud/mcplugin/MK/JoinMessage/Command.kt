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

fun CMD_INFO(sender: CommandSender){

}

fun CMD_GET_GROUPS(sender: CommandSender){

}

fun CMD_ADD_GROUP(sender: CommandSender, args: Array<String>){

}

fun CMD_DEL_GROUP(sender: CommandSender, args: Array<String>){

}

fun CMD_MODIFY_JM_GROUP(sender: CommandSender, args: Array<String>){

}

fun CMD_MODIFY_EM_GROUP(sender: CommandSender, args: Array<String>) {

}

fun CMD_MODIFY_PRIORITY_GROUP(sender: CommandSender, args: Array<String>) {

}

fun CMD_MODIFY_PERM_GROUP(sender: CommandSender, args: Array<String>) {

}