package me.mucloud.plugins.MK.JoinMessage

import java.io.File

interface IConfiguration {

    fun getVersion(): String

    fun getConfigFile(): File

    fun setJoinMessage(message: String?)

    fun setExitMessage(message: String?)

    fun getJoinMessage(): String?

    fun getExitMessage(): String?

    fun getPermissionMessageMap(): MutableList<PermissionMessage>?

    fun comparePriority(pml: MutableList<PermissionMessage>): PermissionMessage

    fun messageChange(message: String): String

}