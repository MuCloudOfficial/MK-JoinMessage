package me.mucloud.plugin.MK.JoinMessage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class Commands implements CommandExecutor {

    private final Main main;

    public Commands(Main plugin){
        main = plugin;
    }

    public void sendInfo(CommandSender sender){
        sender.sendMessage("§7§l| " + main.getPrefix() + "     §7§lVer.§6§l" + main.getConfiguration().getVersion());
        sender.sendMessage("§7§l| §b§l作者: §7§lMu_Cloud");
        sender.sendMessage("§7§l| §4§l开源地址: https://www.github.com/MuCloudOfficial/MK-JoinMessage");
        sender.sendMessage("§7§l| §b§m----------------------------------------------");
        sender.sendMessage("§7§l| §b§l/mkjm reload                §e§l重载本插件");
        sender.sendMessage("§7§l| §b§l/mkjm setJoinMessage        §e§l设置默认加入提示");
        sender.sendMessage("§7§l| §b§l/mkjm setExitMessage        §e§l设置默认退出提示");
        sender.sendMessage("§7§l| §b§l/mkjm setPermissionMessages §e§l加入一个权限进出提示");
        sender.sendMessage("§7§l| §b§l/mkjm delPermissionMessages §e§l删除一个权限进出提示");
        sender.sendMessage("§7§l| §b§m----------------------------------------------");
    }

    @Override public boolean onCommand(CommandSender sender, Command cmd, String s, String[] ss) {
        if(cmd.getName().equalsIgnoreCase("mkjm") || cmd.getName().equalsIgnoreCase("mkjoinmessage")){
            if(!sender.hasPermission("mkjmp.admin")){
                sender.sendMessage("§7§l| §c你没有权限执行此操作");
                return true;
            }
            if(ss.length == 0){
                sendInfo(sender);
                return true;
            }else if(ss.length == 1){
                switch (ss[0].toLowerCase()) {
                    case "info":
                        sendInfo(sender);
                        return true;

                    case "reload":
                        main.onReload();
                        sender.sendMessage("§7§l[" + main.getPrefix() + "§7§l] §a§l插件重载完毕");
                        return true;

                    default:
                        sender.sendMessage("§7§l| §c指令非法，请正确输入指令");
                        return true;
                }
            }else if(ss.length == 2){
                try{
                    FileConfiguration fc = new YamlConfiguration();
                    StringBuilder result = new StringBuilder();

                    switch (ss[0].toLowerCase()){
                        case "setjoinmessage":
                            main.getConfiguration().setJoinMessage(ss[1]);

                            fc.load(main.getConfiguration().getConfigFile());

                            for(String temp : Arrays.asList(ss).subList(1,ss.length - 1)){
                                result.append(" ").append(temp);
                            }

                            fc.set("JoinMessage", result);

                            fc.save(main.getConfiguration().getConfigFile());
                            sender.sendMessage("§7§l| §a已更改了全局进入提示");

                            return true;

                        case "setexitmessage":
                            main.getConfiguration().setExitMessage(ss[1]);

                            fc.load(main.getConfiguration().getConfigFile());

                            for(String temp : Arrays.asList(ss).subList(1,ss.length - 1)){
                                result.append(" ").append(temp);
                            }

                            fc.set("ExitMessage",result);

                            fc.save(main.getConfiguration().getConfigFile());
                            sender.sendMessage("§7§l| §a已更改了全局退出提示");

                            return true;

                        case "delpermissionmessage":
                            for(PermissionMessage pm : main.getConfiguration().getPermissionMessageMap()){
                                if(pm.getPermission().equals(ss[1])){
                                    main.getConfiguration().getPermissionMessageMap().remove(pm);

                                    try {
                                        fc.load(main.getConfiguration().getConfigFile());
                                        Map<String,Object> map = fc.getConfigurationSection("PermissionMessage").getValues(true);
                                        map.remove(ss[1] + ".Priority");
                                        map.remove(ss[1] + ".JoinMessage");
                                        map.remove(ss[1] + ".ExitMessage");
                                        fc.set("PermissionMessage", map);
                                        fc.save(main.getConfiguration().getConfigFile());
                                    } catch (IOException | InvalidConfigurationException e) {
                                        throw new RuntimeException(e);
                                    }

                                    sender.sendMessage("§7§l| §a已删除了一个权限项");
                                    return true;
                                }
                            }
                            sender.sendMessage("§7§l| §a权限集内找不到该权限");
                            return true;
                        default:
                            sender.sendMessage("§7§l| §c指令非法，请正确输入指令");
                            return true;

                    }
                } catch (InvalidConfigurationException | IOException e) {
                    throw new RuntimeException(e);
                }
            }else if(ss.length == 5){
                if(ss[0].equalsIgnoreCase("setPermissionMessages")){
                    main.getConfiguration().getPermissionMessageMap().add(
                            new PermissionMessage(ss[1],Integer.parseInt(ss[2]),ss[3],ss[4]));

                    FileConfiguration fc = new YamlConfiguration();
                    try {
                        fc.load(main.getConfiguration().getConfigFile());
                        Map<String,Object> map = fc.getConfigurationSection("PermissionMessage").getValues(true);
                        map.put(ss[1] + ".Priority", Integer.parseInt(ss[2]));
                        map.put(ss[1] + ".JoinMessage", ss[3]);
                        map.put(ss[1] + ".ExitMessage", ss[4]);
                        fc.set("PermissionMessage", map);
                        fc.save(main.getConfiguration().getConfigFile());
                    } catch (IOException | InvalidConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                }
                sender.sendMessage("§7§l| §a已添加了一个权限项");
                return true;
            }else{
                sender.sendMessage("§7§l[" + main.getPrefix() + "§7§l] §c指令非法，请正确输入指令");
                return true;
            }
        }
        return false;
    }
}
