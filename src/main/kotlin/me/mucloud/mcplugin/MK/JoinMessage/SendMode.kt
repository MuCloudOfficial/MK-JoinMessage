package me.mucloud.mcplugin.MK.JoinMessage

@Deprecated("暂缓 | 当前不可用")
enum class SendMode{

    CHAT,
    ACTIONBAR,
    BOSSBAR;

    companion object{
        fun match(id: String): SendMode{
            when(id.uppercase()){
                "CHAT" -> CHAT
                "ACTIONBAR" -> ACTIONBAR
                "BOSSBAR" -> BOSSBAR
            }
            throw UnsupportedOperationException("不支持的消息显示格式")
        }
    }

}