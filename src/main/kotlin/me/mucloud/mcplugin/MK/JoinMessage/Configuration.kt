package me.mucloud.mcplugin.MK.JoinMessage

import org.yaml.snakeyaml.Yaml

object Configuration {

    private lateinit var Version: String
    private lateinit var VersionCN: String
    private lateinit var VersionView: String
    private lateinit var VersionCNView: String
    private lateinit var AuthorList: List<String>
    private var dev: Int = 0

    private val Conf: MutableMap<String, String> = mutableMapOf(
        Pair("conf.version", "1"),
        Pair("db.autoSave", "true"),
        Pair("plugin.reportToLogFile", "true"),
    )

    internal fun preInit(main: Main){
        if(!main.dataFolder.exists()) main.dataFolder.mkdir()

        Yaml().load<Map<String, Any>>(main.getResource("plugin.yml")).also {
            Version = it["version"] as String
            VersionCN = it["versionCN"] as String
            VersionView = it["versionV"] as String
            VersionCNView = it["versionCNV"] as String
            AuthorList = it["authors"] as List<String>
            dev = it["internalVersion"] as Int
        }

        MessageSender.sendToConsole(MessageLevel.INFO, "正在加载 MK-JoinMessage | $Version | $VersionCN | DEV.$dev")
        MessageSender.sendToConsole(MessageLevel.INFO, "作者：${getAuthorList()}")
        MessageSender.sendToConsole(MessageLevel.INFO, "MADE IN SAKURA OCEAN & BASED ON SPIGOT API")
    }

    internal fun init(){

        MessageSender.sendToConsole(MessageLevel.FINISH, "已加载 Configuration 模块")
    }

    internal fun unInit(){

        MessageSender.sendToConsole(MessageLevel.FINISH, "已卸载 Configuration 模块")
    }

    internal fun getDev(): Int = dev

    internal fun getAuthorList(): String = AuthorList.toString().substring(1).dropLast(1)

    internal fun getConfVer(): Int = Conf["conf.version"]!!.toInt()

    internal fun getConfiguration(): Map<String, String> = Conf

    internal fun save(){

    }

    internal fun getVersion(zh: Boolean = true, isView: Boolean = true) = if(isView){
        if(zh){
            VersionCNView
        }else{
            VersionView
        }
    }else{
        if(zh){
            VersionCN
        }else{
            Version
        }
    }

}