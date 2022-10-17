package me.mucloud.plugins.MK.JoinMessage

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.IOException
import java.util.*

object Commands: CommandExecutor{

    private fun sendInfo(sender: CommandSender){
        sender.sendMessage("§7§l| " + Main.getPrefix() + "     §7§lVer.§6§l" + Configuration.getVersion())
        sender.sendMessage("§7§l| §b§l作者: §7§lMu_Cloud")
        sender.sendMessage("§7§l| §4§l开源地址: https://www.github.com/MuCloudOfficial/MK-JoinMessage")
        sender.sendMessage("§7§l| §b§m----------------------------------------------")
        sender.sendMessage("§7§l| §b§l/mkjm reload                §e§l重载本插件")
        sender.sendMessage("§7§l| §b§l/mkjm setJoinMessage        §e§l设置默认加入提示")
        sender.sendMessage("§7§l| §b§l/mkjm setExitMessage        §e§l设置默认退出提示")
        sender.sendMessage("§7§l| §b§l/mkjm setPermissionMessages §e§l加入一个权限进出提示")
        sender.sendMessage("§7§l| §b§l/mkjm delPermissionMessages §e§l删除一个权限进出提示")
        sender.sendMessage("§7§l| §b§m----------------------------------------------")

    }

    override fun onCommand(sender: CommandSender, cmd: Command, s: String, ss: Array<out String>): Boolean {
        if (cmd.name.equals("mkjm", ignoreCase = true) || cmd.name
                .equals("mkjoinmessage", ignoreCase = true)
        ) {
            if (!sender.hasPermission("mkjmp.admin")) {
                sender.sendMessage("§7§l| §c你没有权限执行此操作")
                return true
            }
            return if (ss.isEmpty()) {
                sendInfo(sender)
                true
            } else if (ss.size == 1) {
                when (ss[0].lowercase(Locale.getDefault())) {
                    "info" -> {
                        sendInfo(sender)
                        true
                    }

                    "reload" -> {
                        Main.onReload()
                        sender.sendMessage("§7§l[" + Main.getPrefix() + "§7§l] §a§l插件重载完毕")
                        true
                    }

                    else -> {
                        sender.sendMessage("§7§l| §c指令非法，请正确输入指令")
                        true
                    }
                }
            } else if (ss.size == 2) {
                try {
                    val fc: FileConfiguration = YamlConfiguration()
                    val result = StringBuilder()
                    when (ss[0].lowercase(Locale.getDefault())) {
                        "setjoinmessage" -> {
                            Main.getConfiguration().setJoinMessage(ss[1])
                            fc.load(Main.getConfiguration().getConfigFile())
                            for (temp in listOf(*ss).subList(1, ss.size - 1)) {
                                result.append(" ").append(temp)
                            }
                            fc["JoinMessage"] = result
                            fc.save(Main.getConfiguration().getConfigFile())
                            sender.sendMessage("§7§l| §a已更改了全局进入提示")
                            true
                        }

                        "setexitmessage" -> {
                            Main.getConfiguration().setExitMessage(ss[1])
                            fc.load(Main.getConfiguration().getConfigFile())
                            for (temp in listOf(*ss).subList(1, ss.size - 1)) {
                                result.append(" ").append(temp)
                            }
                            fc["ExitMessage"] = result
                            fc.save(Main.getConfiguration().getConfigFile())
                            sender.sendMessage("§7§l| §a已更改了全局退出提示")
                            true
                        }

                        "delpermissionmessage" -> {
                            for (pm in Main.getConfiguration().getPermissionMessageMap()!!) {
                                if (pm.getPermission() == ss[1]) {
                                    Main.getConfiguration().getPermissionMessageMap()!!.remove(pm)
                                    try {
                                        fc.load(Main.getConfiguration().getConfigFile())
                                        val map = fc.getConfigurationSection("PermissionMessage")!!
                                            .getValues(true)
                                        map.remove(ss[1] + ".Priority")
                                        map.remove(ss[1] + ".JoinMessage")
                                        map.remove(ss[1] + ".ExitMessage")
                                        fc["PermissionMessage"] = map
                                        fc.save(Main.getConfiguration().getConfigFile())
                                    } catch (e: IOException) {
                                        throw RuntimeException(e)
                                    } catch (e: InvalidConfigurationException) {
                                        throw RuntimeException(e)
                                    }
                                    sender.sendMessage("§7§l| §a已删除了一个权限项")
                                    return true
                                }
                            }
                            sender.sendMessage("§7§l| §a权限集内找不到该权限")
                            true
                        }

                        else -> {
                            sender.sendMessage("§7§l| §c指令非法，请正确输入指令")
                            true
                        }
                    }
                } catch (e: InvalidConfigurationException) {
                    throw RuntimeException(e)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            } else if (ss.size == 5) {
                if (ss[0].equals("setPermissionMessages", ignoreCase = true)) {
                    Main.getConfiguration().getPermissionMessageMap()!!.add(
                        PermissionMessage(ss[1], ss[2].toInt(), ss[3], ss[4])
                    )
                    val fc: FileConfiguration = YamlConfiguration()
                    try {
                        fc.load(Main.getConfiguration().getConfigFile())
                        val map = fc.getConfigurationSection("PermissionMessage")!!
                            .getValues(true)
                        map[ss[1] + ".Priority"] = ss[2].toInt()
                        map[ss[1] + ".JoinMessage"] = ss[3]
                        map[ss[1] + ".ExitMessage"] = ss[4]
                        fc["PermissionMessage"] = map
                        fc.save(Main.getConfiguration().getConfigFile())
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    } catch (e: InvalidConfigurationException) {
                        throw RuntimeException(e)
                    }
                }
                sender.sendMessage("§7§l| §a已添加了一个权限项")
                true
            } else {
                sender.sendMessage("§7§l[" + Main.getPrefix() + "§7§l] §c指令非法，请正确输入指令")
                true
            }
        }
        return false
    }

}