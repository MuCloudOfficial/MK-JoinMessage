package me.mucloud.plugins.MK.JoinMessage

import org.bukkit.ChatColor
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.IOException

object Configuration: IConfiguration{

    private lateinit var Version: String

    private val ConfigFolder: File = Main.dataFolder
    private val ConfigFile: File = File(ConfigFolder, "config.yml")
    private var ConfigVersion: Int? = null

    private var JoinMessage: String? = null
    private var ExitMessage: String? = null
    private var PermissionMessageMap: MutableList<PermissionMessage>? = ArrayList()

    fun process(){
        checkIntegrity()
        loadConfig()
    }

    fun clearConfig(){
        JoinMessage = null
        ExitMessage = null
        PermissionMessageMap = null
    }

    private fun checkIntegrity(){
        if(!ConfigFolder.exists()){
            Main.dataFolder.mkdir()
        }
        if(!ConfigFile.exists()){
            Main.saveDefaultConfig()
        }
    }

    private fun loadConfig(){
        try {
            val fc: FileConfiguration = YamlConfiguration()
            val y = Yaml()
            val Pmap: HashMap<*, *> = y.loadAs(Main.getResource("plugin.yml"), HashMap::class.java)
            val Cmap: HashMap<*, *> = y.loadAs(Main.getResource("config.yml"), HashMap::class.java)

            Version = Pmap["Version"].toString()
            ConfigVersion = Integer.parseInt(Cmap["Version"].toString())

            JoinMessage =
                if (fc.get("JoinMessage") == null) "§7[§a+§7] " + "%player_name%" else fc.getString("JoinMessage")
            ExitMessage =
                if (fc.get("ExitMessage") == null) "§7[§4-§7] " + "%player_name%" else fc.getString("ExitMessage")

            if (fc["PermissionMessages"] != null) {
                val Key_List: MutableList<String> = ArrayList()
                val Permission_Set: MutableSet<String> = HashSet()

                // 第一轮遍历：获取表结构的所有 Keys ，借助末端 Key 选取所需数据
                for (s in fc.getConfigurationSection("PermissionMessages")!!.getKeys(true)) {
                    if (s.contains("Priority") ||
                        s.contains("JoinMessage") ||
                        s.contains("ExitMessage")
                    ) {
                        Key_List.add(s)
                    }
                }

                // 第二轮遍历：拆分，借助集合特性取同类项以获取全部 Permission
                for (s in Key_List) {
                    Permission_Set.add(s.substring(0, s.lastIndexOf(".")))
                }

                // 第三轮遍历：将获取的 Permission 集合遍历，重新使用 FileConfiguration 获取信息并生成 PermissionMessage
                for (s in Permission_Set) {
                    if (fc["PermissionMessages.$s.Priority"] == null || fc["PermissionMessages.$s.JoinMessage"] == null || fc["PermissionMessages.$s.ExitMessage"] == null) {
                        Main.logger.warning("该权限项定义错误 -> $s 该权限项将跳过加载")
                        continue
                    }
                    PermissionMessageMap?.add(
                        PermissionMessage(
                            s,
                            fc.getInt("PermissionMessages.$s.Priority"),
                            fc.getString("PermissionMessages.$s.JoinMessage"),
                            fc.getString("PermissionMessages.$s.ExitMessage")
                        )
                    )
                }
            }

            // 优先级重合检查.
            val pms: MutableSet<Int> = java.util.HashSet()
            for (pm in PermissionMessageMap!!) {
                pms.add(pm.getPriority())
            }

            if (pms.size != PermissionMessageMap!!.size) {
                Main.logger.severe("你的 PermissionMessages 中可能有多个 Permission 对应的优先级重合，请更改优先级设置")
                Main.logger.severe("本次启动不加载 PermissionMessages ")
                PermissionMessageMap!!.clear()
            }
        }catch(e: IOException){
            throw RuntimeException()
        }catch(e: InvalidConfigurationException){
            throw RuntimeException()
        }


    }

    override fun messageChange(message: String): String {
        return ChatColor.translateAlternateColorCodes('&', message)
    }

    override fun getConfigFile(): File {
        return ConfigFile
    }

    override fun setJoinMessage(message: String?) {
        JoinMessage = message?.let { messageChange(it) }
    }

    override fun setExitMessage(message: String?) {
        ExitMessage = message?.let { messageChange(it) }
    }

    override fun getJoinMessage(): String? {
        return JoinMessage
    }

    override fun getExitMessage(): String? {
        return ExitMessage
    }

    override fun getPermissionMessageMap(): MutableList<PermissionMessage>? {
        return PermissionMessageMap
    }

    override fun getVersion(): String {
        return Version
    }

    override fun comparePriority(pml: MutableList<PermissionMessage>): PermissionMessage {
        if (pml.size == 1) {
            return pml[0]
        }
        val i = pml.iterator()
        var result = i.next()
        while (i.hasNext()) {
            val next = i.next()
            if (next.getPriority() > result.getPriority()) {
                result = next
            }
        }
        return result
    }

}