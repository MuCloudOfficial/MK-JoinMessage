package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.Bukkit

object PlaceholderAPIHooker{

    private var PAPI_VER: String = "NULL"

    fun hook(){
        val target = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if(target != null){
            PAPI_VER = target.description.version
        }
    }

    fun isHook(): Boolean {
        return PAPI_VER != "NULL"
    }

}