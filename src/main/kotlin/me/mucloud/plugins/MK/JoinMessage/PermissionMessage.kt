package me.mucloud.plugins.MK.JoinMessage

import org.bukkit.ChatColor

class PermissionMessage constructor(

    permission: String,
    priority: Int,
    joinMessage: String?,
    exitMessage: String?

){

    private var Permission: String = permission
    private var Priority: Int = priority
    private var JoinMessage: String? = joinMessage
    private var ExitMessage: String? = exitMessage

    fun getPermission(): String{
        return Permission
    }

    fun getPriority(): Int{
        return Priority
    }

    fun getJoinMessage(): String{
        return changeMessage(JoinMessage)
    }

    fun getExitMessage(): String{
        return changeMessage(ExitMessage)
    }

    private fun changeMessage(message: String?): String{
        return ChatColor.translateAlternateColorCodes('&', message.toString())
    }

}