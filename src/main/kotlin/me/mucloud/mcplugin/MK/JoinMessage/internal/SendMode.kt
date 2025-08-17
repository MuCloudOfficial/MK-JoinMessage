package me.mucloud.mcplugin.MK.JoinMessage.internal

enum class SendMode{

    NULL, //SPY
    CHAT,
    ACTIONBAR, // todo
    BOSSBAR; // todo

    companion object{
        fun match(id: String): SendMode{
            return when(id.uppercase()){
                "NULL" -> NULL
                "CHAT" -> CHAT
                "ACTIONBAR" -> ACTIONBAR
                "BOSSBAR" -> BOSSBAR
                else -> throw UnsupportedOperationException("不支持的消息显示格式")
            }
        }
    }

}