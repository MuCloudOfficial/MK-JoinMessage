package me.mucloud.plugin.MK.JoinMessage;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Configuration implements IConfiguration{

    private final Main main;
    private final File ConfigFolder;
    private final File ConfigFile;
    private Integer ConfigVersion = null;
    private String Version = null;

    Configuration(Main plugin){
        main = plugin;
        ConfigFolder = main.getDataFolder();
        ConfigFile = new File(ConfigFolder,"config.yml");
    }

    public void onLoad(){
        checkIntegrity();
        loadConfig();
    }

    public void clearConfig(){
        JoinMessage = null;
        ExitMessage = null;
        PermissionMessageMap.clear();
    }

    private void checkIntegrity(){
        if(!ConfigFolder.exists()){
            ConfigFolder.mkdir();
        }
        if(!ConfigFile.exists()){
            main.saveDefaultConfig();
        }else if(ConfigFile.length() == 0){
            ConfigFile.delete();
            main.saveDefaultConfig();
        }
    }

    private String JoinMessage = null;
    private String ExitMessage = null;
    private final List<PermissionMessage> PermissionMessageMap = new ArrayList<>();

    private void loadConfig(){
        FileConfiguration fc = new YamlConfiguration();
        try {
            Yaml y = new Yaml();
            HashMap<?,?> Pmap = y.loadAs(main.getResource("plugin.yml"),HashMap.class);
            HashMap<?,?> Cmap = y.loadAs(main.getResource("config.yml"),HashMap.class);
            Version = Pmap.get("version").toString();
            ConfigVersion = (Integer) Cmap.get("Version");

            fc.load(ConfigFile);

            if(fc.get("Version") == null){
                ConfigFile.delete();
                main.saveDefaultConfig();
            }



            JoinMessage = messageChange(fc.get("JoinMessage") == null ? "§7[§a+§7] " + "%player_name%" : fc.getString("JoinMessage"));
            ExitMessage = messageChange(fc.get("ExitMessage") == null ? "§7[§4-§7] " + "%player_name%" : fc.getString("ExitMessage"));

            if(fc.get("PermissionMessages") != null){

                List<String> Key_List = new ArrayList<>();
                Set<String> Permission_Set = new HashSet<>();

                // 第一轮遍历：获取表结构的所有 Keys ，借助末端 Key 选取所需数据
                for(String s : fc.getConfigurationSection("PermissionMessages").getKeys(true)){
                    if(s.contains("Priority") ||
                            s.contains("JoinMessage") ||
                            s.contains("ExitMessage")){
                        Key_List.add(s);
                    }
                }

                // 第二轮遍历：拆分，借助集合特性取同类项以获取全部 Permission
                for(String s : Key_List){
                    Permission_Set.add(s.substring(0,s.lastIndexOf(".")));
                }

                // 第三轮遍历：将获取的 Permission 集合遍历，重新使用 FileConfiguration 获取信息并生成 PermissionMessage
                for(String s : Permission_Set){
                    if(fc.get("PermissionMessages." + s + ".Priority") == null ||
                            fc.get("PermissionMessages." + s + ".JoinMessage") == null ||
                            fc.get("PermissionMessages." + s + ".ExitMessage") == null){
                        main.getLogger().warning("该权限项定义错误 -> " + s + " 该权限项将跳过加载");
                        continue;
                    }

                    PermissionMessageMap.add(new PermissionMessage(s,
                            fc.getInt("PermissionMessages." + s + ".Priority"),
                            messageChange(fc.getString("PermissionMessages." + s + ".JoinMessage")),
                            messageChange(fc.getString("PermissionMessages." + s + ".ExitMessage"))));

                }

            }

            // 优先级重合检查.
            Set<Integer> pms = new HashSet<>();
            for(PermissionMessage pm : PermissionMessageMap){
                pms.add(pm.getPriority());
            }

            if(pms.size() != PermissionMessageMap.size()){
                main.getLogger().severe("你的 PermissionMessages 中可能有多个 Permission 对应的优先级重合，请更改优先级设置");
                main.getLogger().severe("本次启动不加载 PermissionMessages ");
                PermissionMessageMap.clear();
            }

        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void mergeConfigFile(int oldVersion){
        //TODO
    }



    @Override public String messageChange(String message){
        return ChatColor.translateAlternateColorCodes('&',message);
    }

    @Override public File getConfigFile(){
        return ConfigFile;
    }

    @Override public void setJoinMessage(String message) {
        JoinMessage = messageChange(message);
    }

    @Override public void setExitMessage(String message) {
        ExitMessage = messageChange(message);
    }

    @Override public String getJoinMessage() {
        return JoinMessage;
    }

    @Override public String getExitMessage() {
        return ExitMessage;
    }

    @Override public List<PermissionMessage> getPermissionMessageMap(){
        return PermissionMessageMap;
    }

    @Override public String getVersion(){
        return Version;
    }

    @Override public PermissionMessage comparePriority(List<PermissionMessage> pml) {

        if(pml.size() == 1){
            return pml.get(0);
        }

        Iterator<PermissionMessage> i = pml.iterator();
        PermissionMessage result = i.next();

        while (i.hasNext()){
            PermissionMessage next = i.next();
            if(next.getPriority() > result.getPriority()){
                result = next;
            }
        }

        return result;
    }

}
