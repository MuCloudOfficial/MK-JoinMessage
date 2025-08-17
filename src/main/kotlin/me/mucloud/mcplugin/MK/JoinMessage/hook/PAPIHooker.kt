package me.mucloud.mcplugin.MK.JoinMessage.hook

import me.clip.placeholderapi.PlaceholderAPI
import me.mucloud.mcplugin.MK.JoinMessage.internal.MessageLevel
import me.mucloud.mcplugin.MK.JoinMessage.internal.MessageSender
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class PAPIHooker {

    private var isHooked: Boolean = false

    init {
        checkPapiHook()
    }

    private fun checkPapiHook(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            isHooked = true
            MessageSender.sendToConsole(MessageLevel.FINISH, "已检测到 PlaceholderAPI, 启动 PAPI 附加功能")
        }else{
            MessageSender.sendToConsole(MessageLevel.WARN, "没有检测到 PlaceholderAPI, 已忽略")
        }
    }

    fun String.convert(player: OfflinePlayer): String = if (isHooked) PlaceholderAPI.setPlaceholders(player, this) else this
    fun String.convert(player: Player): String = if (isHooked) PlaceholderAPI.setPlaceholders(player, this) else this

    fun isHooked(): Boolean = isHooked

}