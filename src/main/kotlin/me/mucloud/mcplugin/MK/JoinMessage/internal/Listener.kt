package me.mucloud.mcplugin.MK.JoinMessage.internal

import me.mucloud.mcplugin.MK.JoinMessage.Main
import me.mucloud.mcplugin.MK.JoinMessage.group.GroupManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object Listener: Listener{

    internal fun init(main: Main){
        Bukkit.getPluginManager().registerEvents(this, main)
        MessageSender.sendToConsole(MessageLevel.FINISH, "已加载 Listener 模块")
    }

    internal fun unInit(){
        HandlerList.unregisterAll(this)
        MessageSender.sendToConsole(MessageLevel.FINISH, "已卸载 Listener 模块")
    }

    @EventHandler fun onJoinServerListener(e: PlayerJoinEvent){
        val targetP = e.player
        if(GroupManager.isSpyPlayer(targetP)){
            e.joinMessage = null
        }else{
            var targetG = GroupManager.getGroup(targetP)
            if(targetG == null){
                targetG = GroupManager.DEFAULT_GROUP()
                targetG.addMember(targetP)
            }
            e.joinMessage = MessageSender.convert(targetP, targetG.getJoinMessage())
            Bukkit.getOnlinePlayers().forEach{
                it.playSound(it, targetG.getSound(), 1.0F, 1.0F)
            }
        }
    }

    @EventHandler fun onExitServerListener(e: PlayerQuitEvent){
        val targetP = e.player
        val targetG = GroupManager.getGroup(targetP)
        if(GroupManager.isSpyPlayer(targetP)){
            e.quitMessage = null
        }else{
            e.quitMessage = MessageSender.convert(targetP, targetG!!.getExitMessage())
            Bukkit.getOnlinePlayers().forEach{
                it.playSound(it, targetG.getSound(), 1.0F, 0F)
            }
        }
    }

}