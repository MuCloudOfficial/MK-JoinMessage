package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player

object GroupManager{

    private val POOL: MutableList<Group> = emptyList<Group>().toMutableList()

    private val DEFAULT_GROUP = Group(
        "default", SendMode.CHAT,
        Sound.BLOCK_NOTE_BLOCK_PLING,
        "§7[§a+§7]{player}",
        "§7[§4-§7]{player}"
    )

    private val SPY_USERS = emptyList<Player>().toMutableList()

    private var USER_SIZE = 0;

    fun init(){
        SQLITEConnector.readGroup().forEach {
            addGroup(it)
        }

        SQLITEConnector.readUser()
        SQLITEConnector.readSpy()
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

    fun addSpyPlayer(spy: Player){
        SPY_USERS.add(spy)
    }

    fun isSpyPlayer(target: Player): Boolean = target in SPY_USERS

    fun DEFAULT_GROUP(): Group = DEFAULT_GROUP

    fun size(): Int = POOL.size

    fun userSize(): Int = USER_SIZE

    fun save(){
        SQLITEConnector.flushGroupManager()
    }

    fun POOL() = POOL

}

class Group(
    private var Name: String,
    private var Mode: SendMode,
    private var SOUND: Sound,
    private var JoinMessage: String,
    private var ExitMessage: String,
    private val Member: MutableList<OfflinePlayer> = emptyList<OfflinePlayer>().toMutableList()
){

    fun getName(): String = Name
    fun getJoinMessage(): String = JoinMessage
    fun getExitMessage(): String = ExitMessage
    fun getMode(): SendMode = Mode
    fun getSound(): Sound = SOUND

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
        | 该组的声音: ${SOUND.name}
        | 进服消息: $JoinMessage
        | 退服消息: $ExitMessage
        | 该组当前成员(${Member.size}): ${Member.toString().substring(1).dropLast(1)}
    """.trimIndent()

    fun equalsName(name: String): Boolean{
        return name == Name
    }

    fun contains(player: OfflinePlayer): Boolean = Member.contains(player)

}