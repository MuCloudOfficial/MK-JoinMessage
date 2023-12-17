package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object CommandManager: CommandExecutor{

    override fun onCommand(sender: CommandSender, cmd: Command, s: String, ss: Array<out String>): Boolean {
        if(cmd.name.equals("mkjm", true)){
            if(ss.isEmpty()){
                Command_info.run(sender)
            }else if(ss.size == 1){
                if(ss[0].equals("info", true)){
                    Command_info.run(sender)
                }else if(ss[0].equals("test1", true)){
                    Configuration.Reader.test1()
                }else if(ss[0].equals("test2", true)){
                    Configuration.Reader.test2()
                }else if(ss[0].equals("test3", true)){
                    Configuration.Reader.test3()
                }else if(ss[0].equals("test4", true)){
                    Configuration.Reader.test4()
                }
            }
            return true
        }
        return false
    }

}

// 注意：所有指令不区分大小写（除参数外）

/**
 *  ### 插件命令 info
 *
 *  用于展示插件指令帮助
 *
 *  用法: /mkjm 或 /mkjm info
 *
 *  @since V1
 */
internal object Command_info{

    fun run(sender: CommandSender){
        sender.sendMessage("""
            §7§l| $PREFIX    §7§l版本: ${Configuration.versionCNView}  §7§l|  ${Configuration.versionType}
            §7§l| ==============================
            §7§l| §e§l所有命令不区分大小写,§4§l红色§e§l为必填参数
            §7§l| ==============================
            §7§l| §6§l/mkjm set §4§l[default或其它组名] §4§l[join或exit] §4§l[显示的消息]
            §7§l|   - 设置组的消息,组的对应权限为 "mkjmpg.组名"
            §7§l| §6§l/mkjm del §4§l[default或其它组名] §6§l[join或exit]
            §7§l|   - 删除组或删除组内的 join/exit 消息
            §7§l| §6§l/mkjm reload
            §7§l|   - 执行插件的重载
            §7§l| ==== MADE IN §d§lSAKURA §3§lOCEAN §7§l====
        """.trimIndent())
    }

}


