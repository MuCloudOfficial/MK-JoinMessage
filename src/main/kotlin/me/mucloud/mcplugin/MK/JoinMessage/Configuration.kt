package me.mucloud.mcplugin.MK.JoinMessage

import com.google.gson.JsonParser
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.yaml.snakeyaml.Yaml
import java.net.URI

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
        Pair("db.use", "SQLITE") // SQLITE | YAML(计划的) | MONGO(计划的) | MYSQL(计划的)
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

object Updater {

    private val Source: String = "https://gitee.com/MuCloudOfficial/MK-JoinMessage"

    private val Tags: String = "https://gitee.com/api/v5/repos/MuCloudOfficial/MK-JoinMessage/tags?sort=updated&direction=asc"
    private val Changelog: String = "/raw/Kotlin-dev/CHANGELOG.yml"

    private var RemoteDev: Int = 0
    private var RemoteRel: Int = 0
    private var Type: String = "NULL"

    private var CHANGELOG_READER: Yaml = Yaml()

    private var UpdateTimerTask: BukkitTask? = null

    fun init(main: Main){

        val res = URI(Tags).toURL()
        val json = JsonParser.parseReader(res.openStream().reader())

        json.asJsonArray.forEach {
            val raw = it.asJsonObject.get("name").asString.split(".")
            if (raw[0] == "Dev" && RemoteDev <= 0) {
                RemoteDev = raw[2].toInt()
            }
            if (raw[0] == "Rel" && RemoteRel <= 0) {
                RemoteRel = raw[2].toInt()
            }
            if (raw[2].toInt() == Configuration.getDev()) {
                Type = raw[0]
                return@forEach
            }
        }

        regUpdateTimer(main)
    }

    private fun regUpdateTimer(main: Main){
        UpdateTimerTask = object : BukkitRunnable() {
            override fun run() {
                requestUpdate()
            }
        }.runTaskTimerAsynchronously(main, 0L, 20 *60 *60L)
    }

    private fun requestUpdate(request: CommandSender = Bukkit.getConsoleSender()){
        val cr = CHANGELOG_READER.load<Map<Int, Map<String, List<String>>>>(URI(Source + Changelog).toURL().openStream())
        when(Type){
            "Rel" -> {
                if(Configuration.getDev() < RemoteRel){
                    val msg = StringBuilder("${Main.Prefix(false)} 当前有更新!\n")
                    cr[RemoteRel]?.forEach {
                        when (it.key) {
                            "desc" -> {
                                msg.append("§7| §a正式版更新 DEV ${Configuration.getDev()} -> $RemoteRel")
                                it.value.forEach { s ->
                                    msg.append("$s\n")
                                }
                                msg.append("§7| ==============================\n")
                            }
                            "important" -> {
                                msg.append("§7| §4§l重要:\n")
                                it.value.forEach { s ->
                                    msg.append("§7| - §4$s\n")
                                }
                                msg.append("§7| ==============================\n")
                            }
                            "normal" -> {
                                msg.append("§7| §6§l主要:")
                                it.value.forEach { s ->
                                    msg.append("§7| - §6$s\n")
                                }
                                msg.append("§7| ==============================\n")
                            }
                        }
                    }
                    MessageSender.sendMessage(MessageLevel.INFO, request, msg.toString())
                    request.spigot().sendMessage(TextComponent().also {
                        it.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("点击下载至插件文件夹的 Update 中"))
                        it.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://gitee.com/MuCloudOfficial/MK-NullCMD/releases/latest")
                        it.text = "§7| 下载链接: https://gitee.com/MuCloudOfficial/MK-NullCMD/releases/latest"
                    })
                }else{
                    if(request is ConsoleCommandSender){
                        MessageSender.sendToConsole(MessageLevel.FINISH, "${Main.Prefix(false)} 当前为最新版本")
                    }
                }
            }

            "Dev" -> {
                if(Configuration.getDev() < RemoteDev){
                    val msg = StringBuilder("${Main.Prefix(false)} 当前有更新!\n")
                    cr[RemoteDev]?.forEach {
                        when (it.key) {
                            "desc" -> {
                                msg.append("§7| §a正式版更新 DEV ${Configuration.getDev()} -> $RemoteDev")
                                it.value.forEach { s ->
                                    msg.append("$s\n")
                                }
                                msg.append("§7| ==============================\n")
                            }
                            "important" -> {
                                msg.append("§7| §4§l重要:\n")
                                it.value.forEach { s ->
                                    msg.append("§7| - §4$s\n")
                                }
                                msg.append("§7| ==============================\n")
                            }
                            "normal" -> {
                                msg.append("§7| §6§l主要:")
                                it.value.forEach { s ->
                                    msg.append("§7| - §6$s\n")
                                }
                                msg.append("§7| ==============================\n")
                            }
                        }
                    }
                    MessageSender.sendMessage(MessageLevel.INFO, request, msg.toString())
                    request.spigot().sendMessage(TextComponent().also {
                        it.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("点击查看最新预览版"))
                        it.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://gitee.com/MuCloudOfficial/MK-NullCMD/releases")
                        it.text = "§7| 下载链接: https://gitee.com/MuCloudOfficial/MK-NullCMD/releases"
                    })
                }else{
                    if(request is ConsoleCommandSender){
                        MessageSender.sendToConsole(MessageLevel.FINISH, "${Main.Prefix(false)} 当前为最新版本")
                    }
                }
            }
        }
    }

    internal fun getType(): String = Type

    internal fun unInit(){
        if(UpdateTimerTask != null && !UpdateTimerTask!!.isCancelled){
            UpdateTimerTask!!.cancel()
        }
    }

}