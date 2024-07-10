package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player

object GroupManager{

    private val POOL: MutableList<Group> = emptyList<Group>().toMutableList()

    private val DEFAULT_GROUP = Group(
        "default", SendMode.CHAT
    )

    private val SPY_USERS = emptyList<Player>().toMutableList()

    private var USER_SIZE = 0;

    internal fun init(){
        SQLITEConnector.readGroup().forEach {
            addGroup(it)
        }

        SQLITEConnector.readUser()
        SQLITEConnector.readSpy()
    }

    internal fun unInit(){
        SQLITEConnector.flushGroupManager()
    }

    // 0 = Success | 1 = Already Exist
    internal fun addGroup(group: Group): Int{
        val target = getGroup(group.getName())
        if(target == null){
            POOL.add(group)
            return 0
        }
        return 1
    }

    // 0 - Success | 1 - Not Found
    internal fun delGroup(name: String): Int{
        val target = getGroup(name)
        if (target != null){
            POOL.remove(target)
            return 0
        }
        return 1
    }

    internal fun getGroup(name: String): Group?{
        POOL.forEach{
            if(it.equalsName(name)){
                return it
            }
        }
        return null
    }

    internal fun getGroup(player: OfflinePlayer): Group?{
        POOL.forEach{ g ->
            if(g.contains(player)){
                return g
            }
        }
        return null
    }

    internal fun addSpyPlayer(spy: Player){
        SPY_USERS.add(spy)
    }

    internal fun isSpyPlayer(target: Player): Boolean = target in SPY_USERS

    internal fun DEFAULT_GROUP(): Group = DEFAULT_GROUP

    internal fun size(): Int = POOL.size

    internal fun SPY_SIZE(): Int = SPY_USERS.size

    internal fun POOL(): List<Group> = POOL

    internal fun SPY(): List<Player> = SPY_USERS

}

class Group(
    private var Name: String,
    private var Mode: SendMode = SendMode.CHAT,
    private var SOUND: Sound = Sound.BLOCK_NOTE_BLOCK_PLING,
    private var JoinMessage: String = "§7[§a+§7]{player}",
    private var ExitMessage: String = "§7[§4-§7]{player}",
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

    fun setSound(sound: Sound){
        SOUND = sound
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