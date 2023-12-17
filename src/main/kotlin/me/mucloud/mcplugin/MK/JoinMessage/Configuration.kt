package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.jetbrains.annotations.TestOnly
import java.io.File
import java.util.*

object Configuration{

    internal lateinit var configFile: File
    internal lateinit var configFolder: File
    internal lateinit var configVersion: String
    val configFileCompatibility: (String) -> Boolean = fun(inver: String): Boolean{
        return inver == "1.3"
    }

    internal lateinit var version: String
    internal lateinit var versionCN: String
    internal lateinit var versionType: VerType

    internal lateinit var versionView: String
    internal lateinit var versionCNView: String

    internal val prd: FileConfiguration = YamlConfiguration()
    internal val crd: FileConfiguration = YamlConfiguration()

    fun init(main: Main) {
        configFolder = main.dataFolder.also { if(!it.exists()) it.mkdir() }
        configFile = File(configFolder, "config.yml").also { if (!it.exists()) main.saveDefaultConfig() }

        prd.load(main.getResource("plugin.yml")!!.reader(Charsets.UTF_8))
        crd.load(configFile)

        version = prd.getString("version", "null")!!
        versionCN = prd.getString("versionCN", "null")!!
        versionType = VerType.valueOf(prd.getString("verType", "DEV")!!.uppercase())

        versionView = prd.getString("versionV", "§4§lUNKNOWN §7§l| §4§lCAUTION TO USE")!!
        versionCNView = prd.getString("versionCNV", "§4§l未知 §7§l| §4§l该版本可能非官方 请谨慎使用")!!

        crd.getString("Version", "NULL")!!.also {
            if (!configFileCompatibility(it)) {
                configFile.renameTo(File(configFolder, "config.yml_${Date().time}.bak"))
                main.saveDefaultConfig()
                ConsoleSender.warn("获取到了版本不兼容的设置文件，该配置文件将被备份")
                crd.load(configFile)
            } else {
                configVersion = it
                ConsoleSender.info("设置文件 $configVersion 已准备就绪")
            }
        }

        var readSize = 0

        crd.getConfigurationSection("Groups").also {
            if (it == null) {
                configFile.renameTo(File(configFolder, "config.yml_${Date().time}.bak"))
                main.saveDefaultConfig()
                ConsoleSender.warn("获取到了设置项不兼容的设置文件，该配置文件将被备份")
                crd.load(configFile)
            }

            for (s in it!!.getKeys(false)){
                GroupManager.addGroup(
                    s,
                    crd.getInt("Groups.$s.Priority"),
                    crd.getString("Groups.$s.Permission") ?: continue,
                    crd.getString("Groups.$s.JoinMessage") ?: continue,
                    crd.getString("Groups.$s.ExitMessage") ?: continue,
                )
                readSize++
            }
        }

        ConsoleSender.info("加载了 $readSize 个组")

    }

    object Reader{
        fun getGroup(name: String): Group? = crd.getSerializable("Groups.$name", Group::class.java)

        @TestOnly
        fun test1(){
            crd.getKeys(true).forEach(ConsoleSender::info)
            crd.getValues(true).forEach{ i -> ConsoleSender.info("${i.key}: ${i.value}")}
        }

        @TestOnly
        fun test2(){
            ConsoleSender.info(crd.getConfigurationSection("Groups")!!.getKeys(false).toString())
        }

        @TestOnly
        fun test3(){
            ConsoleSender.info(getGroup("default").toString())
        }

        @TestOnly
        fun test4(){
            Writer.inGroup(Group("wow", 999))
        }
    }

    object Writer{
        fun inGroup(group: Group){
            crd.set("Groups.${group.getName()}", group)
            crd.save(configFile)
        }

        @TestOnly
        fun delGroup(group: Group){
            crd.set(group.getName(), null)
        }
    }

}

enum class VerType(
    private val viewMsg: String
){
    RELEASE("§3§l正式发行版本"), DEV("§4§l开发版本"), BETA("§b§l先行测试版本");

    override fun toString(): String = viewMsg
}