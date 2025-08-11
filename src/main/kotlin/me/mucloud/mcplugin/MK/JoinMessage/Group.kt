package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player

object GroupManager{

    private val POOL: MutableList<Group> = emptyList<Group>().toMutableList()

    private val DEFAULT_GROUP = Group(
        "default", SendMode.CHAT
    )

    private val SPY_GROUP = Group(
        "Spy", SendMode.NULL
    )

    fun init(){

    }

    fun save(){

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

    // 0 - Success | 1 - Not Found | 2 - Not Allowed
    internal fun delGroup(name: String): Int{
        val target = getGroup(name)
        if (target != null){
            if(target == DEFAULT_GROUP){
                return 2
            }
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

    internal fun getGroup(player: Player): Group?{
        POOL.forEach{ g ->
            if(g.contains(player)){
                return g
            }
        }
        return null
    }

    internal fun isSpyPlayer(target: OfflinePlayer): Boolean = SPY_GROUP.contains(target)

    internal fun DEFAULT_GROUP(): Group = DEFAULT_GROUP

    internal fun size(): Int = POOL.size + SPY_GROUP.size() + DEFAULT_GROUP.size()

    internal fun POOL(): List<Group> = POOL

    internal fun SPY(): Group = SPY_GROUP

}

class Group(
    private var Name: String,
    private var Mode: SendMode = SendMode.CHAT,
    private var SOUND: Sound = Sound.BLOCK_NOTE_BLOCK_PLING,
    private var JoinMessage: String = "§7[§a+§7] {player}",
    private var ExitMessage: String = "§7[§4-§7] {player}",
    private val Member: MutableList<OfflinePlayer> = emptyList<OfflinePlayer>().toMutableList()
): Iterable<OfflinePlayer>{

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

    fun addMember(player: OfflinePlayer): Boolean{
        if(contains(player)){
            return false
        }
        Member.add(player)
        return true
    }

    fun delMember(player: OfflinePlayer): Boolean{
        Member.forEach {
            if(player.uniqueId == it.uniqueId){
                Member.remove(it)
                return true
            }
        }
        return false
    }

    fun getMembers(): List<OfflinePlayer> = Member

    fun info(): String {
        val res = StringBuilder()
        res.append("| 组名: $Name\n")
        res.append("| 该组的声音: ${SOUND.name}\n")
        res.append("| 进服消息: $JoinMessage\n")
        res.append("| 退服消息: $ExitMessage\n")
        res.append("| 该组当前成员(${Member.size}):\n| ")
        Member.forEach {
            if(Member.lastIndexOf(it) == 0){
                res.append(it.name)
            }else{
                res.append(it.name + ", ")
            }
        }
        return res.toString()
    }

    fun equalsName(name: String): Boolean{
        return name == Name
    }

    fun contains(player: OfflinePlayer): Boolean{
        Member.forEach {
            if(player.uniqueId == it.uniqueId){
                return true
            }
        }
        return false
    }

    fun size(): Int = Member.size
    override fun iterator(): Iterator<OfflinePlayer> = Member.iterator()

}