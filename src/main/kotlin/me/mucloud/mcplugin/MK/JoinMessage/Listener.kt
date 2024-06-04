package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object Listener: Listener{

    @EventHandler fun onJoinServerListener(e: PlayerJoinEvent){
        val target = GroupManager.getGroup(e.player)
        if(target == GroupManager.SPY_GROUP()){
            e.joinMessage = null
        }else{
            e.joinMessage = target.getJoinMessage()
        }
    }

    @EventHandler fun onExitServerListener(e: PlayerQuitEvent){
        val target = GroupManager.getGroup(e.player)
        if(target == GroupManager.SPY_GROUP()){
            e.quitMessage = null
        }else{
            e.quitMessage = target.getExitMessage()
        }
    }

}