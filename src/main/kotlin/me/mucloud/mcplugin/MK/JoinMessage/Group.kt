package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object GroupManager{

    private val POOL: MutableList<Group> = emptyList<Group>().toMutableList()

    internal val DEFAULT_GROUP = Group(
        "default", SendMode.CHAT,
        "§7[§a+§7]{player}",
        "§7[§4-§7]{player}"
    )

    internal val SPY_USERS = emptyList<Player>().toMutableList()

    internal var USER_SIZE = 0;

    fun init(){
        SQLITEConnector.getGroups()
        SQLITEConnector.getUsers()
        SQLITEConnector.getSpy()
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

    fun size(): Int = POOL.size

    fun userSize(): Int = USER_SIZE

    fun save(){
        SQLITEConnector.flush()
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
        | 组名: $Name
        | 进服消息: $JoinMessage
        | 退服消息: $ExitMessage
        | 该组当前成员(${Member.size}): ${Member.toString().substring(1).dropLast(1)}
    """.trimIndent()

    fun equalsName(name: String): Boolean{
        return name == Name
    }

    fun contains(player: OfflinePlayer): Boolean = Member.contains(player)

}