package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.OfflinePlayer

object GroupManager{

    private val POOL: MutableList<Group> = emptyList<Group>().toMutableList()

    private val DEFAULT_GROUP: Group = Group(
        "default", SendMode.CHAT,
        "§7[§a+§7]§f {player}",
        "§7[§4-§7]§f {player}"
    )

    private val SPY_GROUP: Group = Group(
        "spy", SendMode.NULL,
        "",""
    )

    fun init(){

    }

    // 0 = Success | 1 = Already Exist
    fun addGroup(group: Group): Int{
        val target = getGroup(group.getName())
        if(target == null){
            POOL.add(group)
            return 0
        }
        return 1
    }

    // 0 - Success | 1 - Not Found
    fun delGroup(name: String): Int{
        val target = getGroup(name)
        if (target != null){
            POOL.remove(target)
            return 0
        }
        return 1
    }

    fun getGroup(name: String): Group?{
        POOL.forEach{
            if(it.equalsName(name)){
                return it
            }
        }
        return null
    }

    fun getGroup(player: OfflinePlayer): Group?{
        POOL.forEach{ g ->
            if(g.contains(player)){
                return g
            }
        }
        return null
    }

}

class Group(
    private var Name: String,
    private var Mode: SendMode,
    private var JoinMessage: String,
    private var ExitMessage: String,
    private val Member: MutableList<OfflinePlayer> = emptyList<OfflinePlayer>().toMutableList()
){

    fun getName(): String = Name
    fun getJoinMessage(): String = JoinMessage
    fun getExitMessage(): String = ExitMessage
    fun getMode(): SendMode = Mode

    fun setJoinMessage(msg: String){
        JoinMessage = msg
    }

    fun setExitMessage(msg: String){
        ExitMessage = msg
    }

    fun addMember(player: OfflinePlayer){
        Member.add(player)
    }

    fun info(): String = """
        
    """.trimIndent()

    fun equalsName(name: String): Boolean{
        return name == Name
    }

    fun contains(player: OfflinePlayer): Boolean = Member.contains(player)

}