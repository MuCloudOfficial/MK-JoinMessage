package me.mucloud.mcplugin.MK.JoinMessage.group

import me.mucloud.mcplugin.MK.JoinMessage.internal.SendMode
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player

class Group(
    private var Name: String,
    private var Mode: SendMode = SendMode.CHAT,
    private var SOUND: Sound = Sound.BLOCK_NOTE_BLOCK_PLING,
    private var JoinMessage: String = "§7[§a+§7] {player}",
    private var ExitMessage: String = "§7[§4-§7] {player}",
    private val Member: MutableList<Player> = mutableListOf()
): Iterable<Player>{

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

    fun addMember(player: Player): Boolean{
        if(contains(player)){
            return false
        }
        Member.add(player)
        return true
    }

    fun delMember(player: Player): Boolean{
        Member.forEach {
            if(player.uniqueId == it.uniqueId){
                Member.remove(it)
                return true
            }
        }
        return false
    }

    fun getMembers(): List<Player> = Member

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
    override fun iterator(): Iterator<Player> = Member.iterator()

}