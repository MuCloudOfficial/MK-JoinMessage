package me.mucloud.plugin.MK.JoinMessage;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static final String Prefix = "§b§lMK§7§l-§e§lJoinMessage";
    private final Configuration Config = new Configuration(this);
    private final Listener Listeners = new Listeners(this);

    @Override public void onEnable(){

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(Listeners, this);
        } else {
            throw new RuntimeException("未检测到 PlaceholderAPI ,这将导致插件加载错误");
        }

        getServer().getConsoleSender().sendMessage("§7§l[" + getPrefix() + "§7§l] §a§l正在启动");
        getCommand("mkjm").setExecutor(new Commands(this));
        getServer().getPluginManager().registerEvents(Listeners,this);
        Config.onLoad();
    }

    @Override public void onDisable(){
        getServer().getConsoleSender().sendMessage("§7§l[" + getPrefix() + "§7§l] §a§l正在关闭");
        getCommand("mkjm").setExecutor(null);
        HandlerList.unregisterAll(this);
        Config.clearConfig();
    }

    public void onReload(){
        Config.clearConfig();
        Config.onLoad();
        getServer().getConsoleSender().sendMessage("§7§l[" + getPrefix() + "§7§l] §a§l重载完毕");
    }

    public String getPrefix(){
        return Prefix;
    }

    public IConfiguration getConfiguration(){
        return Config;
    }

}
