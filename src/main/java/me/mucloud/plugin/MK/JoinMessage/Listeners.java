package me.mucloud.plugin.MK.JoinMessage;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class Listeners implements Listener {

    private final Main Main;

    Listeners(Main plugin){
        Main = plugin;
    }

    @EventHandler public void JoinServerListener(PlayerJoinEvent pje){

        Player targetP = pje.getPlayer();

        List<PermissionMessage> matchedList = new ArrayList<>();
        for(PermissionMessage pm : Main.getConfiguration().getPermissionMessageMap()){

            // 当设置文件中两种模式都有定义时，PermissionMessages 拥有检查最高优先级
            if(Main.getConfiguration().getPermissionMessageMap().size() == 0){
                break;
            }

            if(targetP.hasPermission(pm.getPermission())){
                matchedList.add(pm);
            }

        }

        // 如果该玩家与 PermissionMessages 中定义的任何项中的 Permission 都不重合，则仍按照普通玩家进出消息推送
        if(matchedList.size() == 0){
            pje.setJoinMessage(
                    PlaceholderAPI.setPlaceholders(targetP,
                            Main.getConfiguration().getJoinMessage()));

            return;
        }

        pje.setJoinMessage(
                PlaceholderAPI.setPlaceholders(targetP,
                    Main.getConfiguration().comparePriority(matchedList).getJoinMessage()));
    }

    @EventHandler public void ExitServerListener(PlayerQuitEvent pqe){

        Player targetP = pqe.getPlayer();

        List<PermissionMessage> matchedList = new ArrayList<>();
        for(PermissionMessage pm : Main.getConfiguration().getPermissionMessageMap()){

            // 当设置文件中两种模式都有定义时，PermissionMessages 拥有检查最高优先级
            if(Main.getConfiguration().getPermissionMessageMap().size() == 0){
                break;
            }

            if(targetP.hasPermission(pm.getPermission())){
                matchedList.add(pm);
            }

        }

        // 如果该玩家与 PermissionMessages 中定义的任何项中的 Permission 都不重合，则仍按照普通玩家进出消息推送
        if(matchedList.size() == 0){
            pqe.setQuitMessage(
                    PlaceholderAPI.setPlaceholders(targetP,
                            Main.getConfiguration().getExitMessage()));
            return;
        }

        pqe.setQuitMessage(
                PlaceholderAPI.setPlaceholders(targetP,
                        Main.getConfiguration().comparePriority(matchedList).getExitMessage()));
    }

}
