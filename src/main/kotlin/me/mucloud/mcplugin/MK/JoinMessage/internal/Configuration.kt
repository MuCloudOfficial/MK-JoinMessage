package me.mucloud.mcplugin.MK.JoinMessage.internal

import me.mucloud.mcplugin.MK.JoinMessage.Main
import kotlin.text.get

object Configuration {

    internal fun preInit(main: Main){
        if(!main.dataFolder.exists()) main.dataFolder.mkdir()
    }

    internal fun init(){

        MessageSender.sendToConsole(MessageLevel.FINISH, "已加载 Configuration 模块")
    }

    internal fun unInit(){

        MessageSender.sendToConsole(MessageLevel.FINISH, "已卸载 Configuration 模块")
    }

    internal fun save(){

    }

}