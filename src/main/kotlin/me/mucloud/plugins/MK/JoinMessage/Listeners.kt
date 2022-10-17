package me.mucloud.plugins.MK.JoinMessage

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object Listeners: Listener{

    @EventHandler fun onPlayerJoin(pje: PlayerJoinEvent){
        val targetP = pje.player

        val matchedList: MutableList<PermissionMessage> = ArrayList()
        for (pm in Main.getConfiguration().getPermissionMessageMap()!!) {

            // 当设置文件中两种模式都有定义时，PermissionMessages 拥有检查最高优先级
            if (Main.getConfiguration().getPermissionMessageMap()!!.size == 0) {
                break
            }
            if (targetP.hasPermission(pm.getPermission())) {
                matchedList.add(pm)
            }
        }

        // 如果该玩家与 PermissionMessages 中定义的任何项中的 Permission 都不重合，则仍按照普通玩家进出消息推送
        if (matchedList.size == 0) {
            pje.joinMessage = PlaceholderAPI.setPlaceholders(
                targetP,
                Main.getConfiguration().getJoinMessage()!!
            )
            return
        }

        pje.joinMessage = PlaceholderAPI.setPlaceholders(
            targetP,
            Main.getConfiguration().comparePriority(matchedList).getJoinMessage()
        )

    }

    @EventHandler fun onPlayerExit(pqe: PlayerQuitEvent){
        val targetP = pqe.player

        val matchedList: MutableList<PermissionMessage> = java.util.ArrayList()
        for (pm in Main.getConfiguration().getPermissionMessageMap()!!) {

            // 当设置文件中两种模式都有定义时，PermissionMessages 拥有检查最高优先级
            if (Main.getConfiguration().getPermissionMessageMap()!!.size == 0) {
                break
            }
            if (targetP.hasPermission(pm.getPermission())) {
                matchedList.add(pm)
            }
        }

        // 如果该玩家与 PermissionMessages 中定义的任何项中的 Permission 都不重合，则仍按照普通玩家进出消息推送
        if (matchedList.size == 0) {
            pqe.quitMessage = PlaceholderAPI.setPlaceholders(
                targetP,
                Main.getConfiguration().getExitMessage()!!
            )
            return
        }

        pqe.quitMessage = PlaceholderAPI.setPlaceholders(
            targetP,
            Main.getConfiguration().comparePriority(matchedList).getExitMessage()
        )

    }

}