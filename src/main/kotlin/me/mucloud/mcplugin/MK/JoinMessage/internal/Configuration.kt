package me.mucloud.mcplugin.MK.JoinMessage.internal

import me.mucloud.mcplugin.MK.JoinMessage.Main
import me.mucloud.mcplugin.MK.JoinMessage.group.Group
import me.mucloud.mcplugin.MK.JoinMessage.group.GroupManager
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object Configuration {

    private lateinit var ConfigFile: File
    private lateinit var Config: YamlConfiguration

    private var checkUpdate: Boolean = true
    private var allowSpy: Boolean = false
    private var GroupSection: List<Group> = GroupManager.POOL()

    internal fun preInit(main: Main){
        if(!main.dataFolder.exists()) main.dataFolder.mkdir()
        ConfigFile = File(main.dataFolder, "config.yml")


        if(!ConfigFile.exists()){
            ConfigFile.parentFile.mkdirs()
            ConfigFile.createNewFile()
            Config = YamlConfiguration.loadConfiguration(ConfigFile).also { it.options().parseComments(true) }.apply{
                set("checkUpdate", checkUpdate)
                set("allowSpy", allowSpy)
                set("Groups", GroupSection)
                setComments("checkUpdate",listOf("是否检查更新"))
                setComments("allowSpy", listOf("是否允许可静默进入服务器的用户组"))
                setComments("Groups", listOf("用户组"))
                save(ConfigFile)
            }
        }else{
            Config = YamlConfiguration.loadConfiguration(ConfigFile).also { it.options().parseComments(true) }
        }
    }

    internal fun init(){
        Config.apply { load(ConfigFile)
            checkUpdate = getBoolean("checkUpdate")
            allowSpy = getBoolean("allowSpy")
           val rawGroupConf = getConfigurationSection("Groups")
            if(rawGroupConf == null){
                GroupSection = GroupManager.POOL()
            }else{
                rawGroupConf.getValues(false) .forEach { g ->
                    when(g.key.split(".").last()){
                        "DEFAULT" -> insertUser2Group(g.key, GroupManager.DEFAULT_GROUP())
                        "SPY" -> insertUser2Group(g.key, GroupManager.SPY())
                        else -> insertUser2Group(g.key,
                            Group(
                                g.key.split(".").last(),
                                SOUND = org.bukkit.Sound.valueOf(getString("${g.key}.Sound") ?: "BLOCK_NOTE_BLOCK_PLING"),
                                JoinMessage = getString("${g.key}.JoinMessage") ?: "§7[§a+§7] {player}",
                                ExitMessage = getString("${g.key}.ExitMessage") ?: "§7[§4-§7] {player}"
                            )
                        )
                    }
                }
            }
        }
    }

    internal fun insertUser2Group(gKey: String, group: Group){
        Config.getStringList("${gKey}.Members").forEach{ m ->
            group.addMember(Bukkit.getPlayer(m) ?: return@forEach) //todo("Msg/Logger")
        }
    }

    internal fun unInit(){
        save()
    }

    internal fun save() = Config.save(ConfigFile)

}