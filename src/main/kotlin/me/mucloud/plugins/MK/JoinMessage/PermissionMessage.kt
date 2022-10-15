package me.mucloud.plugins.MK.JoinMessage

import org.bukkit.ChatColor

class PermissionMessage constructor(

    permission: String,
    priority: Int,
    joinMessage: String,
    exitMessage: String

){

    var Permission: String = permission
    var Priority: Int = priority

    var JoinMessage: String = joinMessage
        get() = changeMessage(field)
    var ExitMessage: String = exitMessage
        get() = changeMessage(field)

    private fun changeMessage(message: String): String{
        return ChatColor.translateAlternateColorCodes('&',message)
    }

}