package me.mucloud.plugins.MK.JoinMessage

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object Command: CommandExecutor{

    private fun sendInfo(sender: CommandSender){
        sender.sendMessage("§7§l| " + Main.getPrefix() + "     §7§lVer.§6§l" + main.getConfiguration().getVersion())
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

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {


        return false
    }


}