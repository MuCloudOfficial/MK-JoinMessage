package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.Bukkit
import java.util.*
import java.util.regex.Pattern

class NMS_HOOKER{

    private val VERSION_PATTERN: Pattern = Pattern.compile("(v|)[0-9][_.][0-9]+[_.][R0-9]*")
    private val supportedVersions: MutableMap<String, Int> = TreeMap(String.CASE_INSENSITIVE_ORDER)

    private val NMS_CLASS: String = "net.minecraft.server"
    private val NMN_CLASS: String = "net.minecraft.network"
    private val CB_CLASS: String = "org.bukkit.craftbukkit"

    private val NMS_VER: String
    private val SERVER_PREFIX: String
    private val SERVER_VER: String

    init{
        supportedVersions["v1_9_R2"] = 1
        supportedVersions["v1_10_R1"] = 2
        supportedVersions["v1_11_R1"] = 3
        supportedVersions["v1_12_R1"] = 4
        supportedVersions["v1_13_R1"] = 5
        supportedVersions["v1_13_R2"] = 6
        supportedVersions["v1_14_R1"] = 7
        supportedVersions["v1_15_R1"] = 8
        supportedVersions["v1_16_R1"] = 9
        supportedVersions["v1_17_R1"] = 10
        supportedVersions["v1_18_R1"] = 11
        supportedVersions["v1_19_R1"] = 12
        supportedVersions["v1_20_R1"] = 13

        SERVER_PREFIX = Bukkit.getServer().version
        SERVER_VER = Bukkit.getServer().bukkitVersion

        NMS_VER = Bukkit.getServer().javaClass.`package`.name.let {
            val raw = it.substring(it.lastIndexOf(".") + 1)
            if(!VERSION_PATTERN.matcher(raw).matches()){
                return@let ""
            }else{
                return@let raw
            }
        }
    }

}