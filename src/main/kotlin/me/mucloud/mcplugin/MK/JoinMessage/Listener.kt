package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object Listener: Listener{

    @EventHandler fun onJoinServerListener(e: PlayerJoinEvent){
        val targetP = e.player
        val targetG = GroupManager.getGroup(targetP) ?: GroupManager.DEFAULT_GROUP()
        if(GroupManager.isSpyPlayer(targetP)){
            e.joinMessage = null
        }else{
            e.joinMessage = targetG.getJoinMessage()
            Bukkit.getOnlinePlayers().forEach{
                it.playSound(it, targetG.getSound(), 1.0F, 1.0F)
            }
        }
    }

    @EventHandler fun onExitServerListener(e: PlayerQuitEvent){
        val targetP = e.player
        val targetG = GroupManager.getGroup(targetP) ?: GroupManager.DEFAULT_GROUP()
        if(GroupManager.isSpyPlayer(targetP)){
            e.quitMessage = null
        }else{
            e.quitMessage = targetG.getExitMessage()
            Bukkit.getOnlinePlayers().forEach{
                it.playSound(it, targetG.getSound(), 1.0F, 0F)
            }
        }
    }

}