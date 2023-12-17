package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player

const val defaultJoinMessage: String = "§7[§3+§7] {player}"
const val defaultExitMessage: String = "§7[§4-§7] {player}"

object GroupManager{

    private var POOL: MutableList<Group> = emptyList<Group>().toMutableList()

    fun addGroup(name: String, priority: Int, joinMessage: String, exitMessage: String): Boolean{
        POOL.forEach{ if(it.getName() == name || it.getPriority() == priority) return false }
        POOL.add(Group(name, priority, joinMessage, exitMessage))
        return true
    }

    fun addGroup(name: String, priority: Int, permission: String, joinMessage: String, exitMessage: String): Boolean{
        POOL.forEach{ if(it.getName() == name || it.getPriority() == priority) return false }
        POOL.add(Group(name, priority, joinMessage, exitMessage))
        return true
    }

    fun delGroup(name: String): Boolean{
        POOL.forEach { i ->
            if(i.getName() == name){
                POOL.remove(i)
                Configuration.Writer.delGroup(i)
                return true
            }
        }
        return false
    }

}

data class Group(
    private var name: String,
    private var priority: Int
): ConfigurationSerializable{

    private var joinMessage: String = defaultJoinMessage
    private var exitMessage: String = defaultExitMessage
    private var permission: String = "mkjmpg.$name"

    init {
        Configuration.Writer.inGroup(this)
    }

    constructor(name: String, priority: Int, joinMessage: String, exitMessage: String): this(name, priority){
        this.joinMessage = joinMessage
        this.exitMessage = exitMessage
    }

    constructor(name: String, priority: Int, joinMessage: String, exitMessage: String, perm: String): this(name, priority, joinMessage, exitMessage){
        permission = perm
    }

    fun getName(): String{
        return name
    }

    fun getJoinMessage(): String{
        return joinMessage
    }

    fun getExitMessage(): String{
        return exitMessage
    }

    fun setJoinMessage(msg: String){
        joinMessage = msg
    }

    fun setExitMessage(msg: String){
        exitMessage = msg
    }

    fun matchPermission(target: Player): Boolean{
        return target.hasPermission(permission)
    }

    fun getPermission(): String{
        return permission
    }

    fun getPriority(): Int{
        return priority
    }

    // 用于序列化，因为没研究明白暂时没啥卵用
    override fun serialize(): Map<String, Any> = mapOf(
        Pair("Permission", permission),
        Pair("Priority", priority),
        Pair("JoinMessage", joinMessage),
        Pair("ExitMessage", exitMessage),
    )

    // 用于反序列化，因为没研究明白暂时没啥卵用
    fun deserialize(map: Map<String, Any>): Group? {
        for(s in map){
            val sv: Map<String, Any> = s.value as Map<String, Any>
            return Group(s.key, sv["Priority"] as Int, sv["Permission"] as String, sv["JoinMessage"] as String, sv["ExitMessage"] as String)
        }
        return null
    }

    override fun toString(): String {
        return "$name|$priority|$permission|$joinMessage|$exitMessage"
    }

}