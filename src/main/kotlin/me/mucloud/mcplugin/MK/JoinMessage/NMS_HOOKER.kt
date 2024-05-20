package me.mucloud.mcplugin.MK.JoinMessage

//TODO("暂缓 | 等下一版本")
class NMS_HOOKER{

    init{
        NMS_CLASS = "net.minecraft.network"
        NMN_CLASS = "net.minecraft.server"
    }

    companion object{

        private lateinit var NMS_CLASS: String
        private lateinit var NMN_CLASS: String

        fun hook(){

        }

    }

}