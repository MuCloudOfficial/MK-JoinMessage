package me.mucloud.mcplugin.MK.JoinMessage

enum class SendMode{

    NULL,
    CHAT,
    ACTIONBAR,
    BOSSBAR;

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