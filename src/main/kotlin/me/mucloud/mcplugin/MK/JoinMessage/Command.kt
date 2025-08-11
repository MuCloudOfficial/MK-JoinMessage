package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object CommandManager: CommandExecutor{

    private var MAIN: Main? = null

    internal fun init(main: Main){
        MAIN = main
        MAIN!!.getCommand("mkjm")!!.setExecutor(this)
        MessageSender.sendToConsole(MessageLevel.FINISH, "已加载 Command 模块")
    }

    internal fun unInit(){
        if(MAIN != null){
            MAIN!!.getCommand("mkjm")!!.setExecutor(null)
            MessageSender.sendToConsole(MessageLevel.FINISH, "已卸载 Command 模块")
        }else{
            MessageSender.sendToConsole(MessageLevel.WARN, "未加载 Command 模块，跳过关闭")
        }
    }

    override fun onCommand(sender: CommandSender, cmd: Command, s: String, ss: Array<out String>): Boolean {
        if(cmd.name.equals("mkjm", true)){
            if(!sender.hasPermission("mkjm.admin")){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "你没有权限执行此操作")
                return true
            }
            if(ss.isEmpty()){
                CMD_info(sender)
            }else{
                when(ss[0].lowercase()){
                    "info" -> CMD_info(sender)
                    "reload" -> CMD_reload(sender)
                    "listgroup" -> CMD_listGroup(sender)
                    "addgroup" -> CMD_addGroup(sender, ss.drop(1))
                    "delgroup" -> CMD_delGroup(sender, ss.drop(1))
                    "infogroup" -> CMD_infoGroup(sender, ss.drop(1))
                    "setjoinmessage" -> CMD_setJoinMessage(sender, ss.drop(1))
                    "setexitmessage" -> CMD_setExitMessage(sender, ss.drop(1))
                    "setsound" -> CMD_setSound(sender, ss.drop(1))
                    "setplayer" -> CMD_setPlayer(sender, ss.drop(1))
                    "setspy" -> CMD_setSpy(sender, ss.drop(1))
                    "listspy" -> CMD_listSpy(sender)
                    else -> {
                        MessageSender.sendMessage(MessageLevel.ERR, sender, "未知的子指令")
                    }
                }
            }
            return true
        }
        return false
    }

    /**
     * ### /mkjm info
     *
     * | 显示插件版本和指令信息
     *
     */
    private fun CMD_info(sender: CommandSender){
        MessageSender.sendMessage(MessageLevel.NULL, sender, """

                §7§l| ${Main.Prefix(false)}  §a${Configuration.getVersion(zh = false, isView = true)} | ${Configuration.getVersion()}    §r§7第 §4${Configuration.getDev()} §7开发版本
                §7§l| §6作者：${Configuration.getAuthorList()}
                §7§l| ===================================================
                §7§l| §b项目站：https://gitee.com/MuCloudOfficial/${Main.Prefix(true)}
                §7§l| §b项目站：https://github.com/MuCloudOfficial/${Main.Prefix(true)}
                §7§l| ===================================================
                §7§l| §4对本插件的所有建议与 BUG 提交务必提交 ISSUE 至项目站，这对插件的优化改良极为重要
                §7§l| ===================================================
                §7§l| §a通用组件指令:
                §7§l| §6/mkjm info                   §a显示此消息
                §7§l| §6/mkjm reload                 §a重载本插件
                §7§l| §6/mkjm listGroup              §a查看当前所有组
                §7§l| ===================================================
                §7§l| §b高级组件指令: 当数据库为 SQLITE 时可以使用
                §7§l| §b/mkjm addGroup [组名]         §b添加一个组
                §7§l| §b/mkjm delGroup [组名]         §b删除一个组
                §7§l| §b/mkjm infoGroup [组名]         §b获取指定组的信息
                §7§l| §b/mkjm setJoinMessage [组名] [进服消息]         §b给指定组设置进服消息
                §7§l| §b/mkjm setExitMessage [组名] [进服消息]         §b给指定组设置离服消息
                §7§l| §b/mkjm setSound [组名] [声音键名]        §b设置该组进服时的声音
                §7§l| §b/mkjm setPlayer [玩家名] [组名]         §b指定一个玩家进入组
                §7§l| §b/mkjm setSpy [玩家]        §b将指定的用户设置为静默进服
                §7§l| §b/mkjm listSpy        §b列出静默进服玩家
                §7§l| ==== MADE IN SAKURA OCEAN & BASED ON SPIGOT API ====
            """.trimIndent())
    }

    /**
     * ### /mkjm list
     *
     * | 列出当前所有组
     *
     */
    private fun CMD_listGroup(sender: CommandSender){
        MessageSender.sendMessage(MessageLevel.INFO, sender, "当前组列表(${GroupManager.size()}):")
        var p = 1
        GroupManager.POOL().forEach {
            MessageSender.sendMessage(MessageLevel.INFO, sender, "$p. ${it.getName()}")
            p++
        }
    }

    private fun CMD_reload(sender: CommandSender){
        GroupManager.save()
        Configuration.unInit()

        Configuration.init()
        GroupManager.init()
        MessageSender.sendMessage(MessageLevel.FINISH, sender, "${Main.Prefix(false)} §a重载完毕")
    }

    private fun CMD_addGroup(sender: CommandSender, args: List<String>){
        if(args.size == 1){
            if(GroupManager.addGroup(Group(args[0])) == 1){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该组已存在")
                return
            }
            MessageSender.sendMessage(MessageLevel.FINISH, sender, "成功添加了 ${args[0]} 组")
            MessageSender.sendMessage(MessageLevel.FINISH, sender, "使用 /mkjm setJoinMessage ${args[0]} 设置进服消息")
            MessageSender.sendMessage(MessageLevel.FINISH, sender, "使用 /mkjm setExitMessage ${args[0]} 设置离服消息")
        }else{
            MessageSender.sendMessage(MessageLevel.ERR, sender, "参数错误")
        }
    }

    private fun CMD_delGroup(sender: CommandSender, args: List<String>){
        if(args.size == 1){
            val res = GroupManager.delGroup(args[0])
            if(res == 1){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该组不存在")
                return
            }else if(res == 2){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "default 组不可删除")
                return
            }
            MessageSender.sendMessage(MessageLevel.FINISH, sender, "成功删除了 ${args[0]} 组")
        }else{
            MessageSender.sendMessage(MessageLevel.ERR, sender, "参数错误")
        }
    }

    private fun CMD_infoGroup(sender: CommandSender, args: List<String>){
        if(args.size == 1){
            val res = GroupManager.getGroup(args[0])
            if(res == null){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该组不存在")
                return
            }
            MessageSender.sendMessage(MessageLevel.NULL, sender, res.info())
        }else{
            MessageSender.sendMessage(MessageLevel.ERR, sender, "参数错误")
        }
    }

    private fun CMD_setJoinMessage(sender: CommandSender, args: List<String>){
        if(args.size == 2){
            val g = GroupManager.getGroup(args[0])
            if(g == null){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该组不存在")
                return
            }else{
                g.setJoinMessage(args[1])
                MessageSender.sendMessage(MessageLevel.FINISH, sender, "该组设置进服消息成功")
            }
        }else{
            MessageSender.sendMessage(MessageLevel.ERR, sender, "参数错误")
        }
    }

    private fun CMD_setExitMessage(sender: CommandSender, args: List<String>){
        if(args.size == 2){
            val g = GroupManager.getGroup(args[0])
            if(g == null){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该组不存在")
                return
            }else{
                g.setExitMessage(args[1])
                MessageSender.sendMessage(MessageLevel.FINISH, sender, "该组设置离服消息成功")
            }
        }else{
            MessageSender.sendMessage(MessageLevel.ERR, sender, "参数错误")
        }
    }

    private fun CMD_setSound(sender: CommandSender, args: List<String>){
        if(args.size == 2){
            val g = GroupManager.getGroup(args[0])
            if(g == null){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该组不存在")
                return
            }else{
                try{
                    g.setSound(Sound.valueOf(args[1]))
                    MessageSender.sendMessage(MessageLevel.FINISH, sender, "该组设置声音成功")
                }catch (e: IllegalArgumentException){
                    MessageSender.sendMessage(MessageLevel.ERR, sender, "输入的声音键名非法")
                    return
                }
            }
        }else{
            MessageSender.sendMessage(MessageLevel.ERR, sender, "参数错误")
        }
    }

    private fun CMD_setPlayer(sender: CommandSender, args: List<String>){
        if(args.size == 2){
            val p = Bukkit.getServer().getPlayer(args[0])
            val g = GroupManager.getGroup(args[1])
            if(p == null){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该用户不存在")
                return
            }
            if(g == null){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该组不存在")
                return
            }
            val og = GroupManager.getGroup(p)
            if(og == null || GroupManager.isSpyPlayer(p)){
                GroupManager.SPY().delMember(p)
            }else{
                if(og.equalsName(g.getName())){
                    MessageSender.sendMessage(MessageLevel.ERR, sender, "该玩家已在该组中")
                    return
                }else{
                    og.delMember(p)
                }
            }
            g.addMember(p)
            MessageSender.sendMessage(MessageLevel.FINISH, sender, "已成功将 ${p.name} 转移至 ${g.getName()} 组")
        }
    }

    private fun CMD_setSpy(sender: CommandSender, args: List<String>){
        if(args.size == 1){
            val p = Bukkit.getServer().getPlayer(args[0])
            if(p == null){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该用户不存在")
                return
            }
            if(GroupManager.isSpyPlayer(p)){
                MessageSender.sendMessage(MessageLevel.ERR, sender, "该玩家已经是静默状态")
                return
            }
            GroupManager.getGroup(p)!!.delMember(p)
            GroupManager.SPY().addMember(p)
            MessageSender.sendMessage(MessageLevel.FINISH, sender, "已成功将 ${p.name} 设置为静默状态")
        }
    }

    private fun CMD_listSpy(sender: CommandSender){
        MessageSender.sendMessage(MessageLevel.INFO, sender, "当前静默状态玩家(${GroupManager.SPY().size()}):")
        val res = StringBuilder()
        GroupManager.SPY().forEach {
            if(GroupManager.SPY().lastIndexOf(it) == 0){
                res.append(it.name)
            }else{
                res.append(it.name + ", ")
            }
        }
        MessageSender.sendMessage(MessageLevel.INFO, sender, res.toString())
    }

}



