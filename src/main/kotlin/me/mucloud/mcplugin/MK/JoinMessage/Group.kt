package me.mucloud.mcplugin.MK.JoinMessage

import org.bukkit.OfflinePlayer

object GroupManager{

    private val POOL: MutableList<Group> = emptyList<Group>().toMutableList()

    fun init(){

    }

    // 0 = Success | 1 = Already Exist
    fun addGroup(G: Group): Int{
        POOL.forEach {
            if(it.equalsName(G.getName())){
                return 1
            }
        }
        return 0
    }

    // 0 - Success | 1 - Not Found
    fun delGroup(name: String): Int{
        POOL.forEach {
            if(it.equalsName(name)){
                POOL.remove(it)
                return 0
            }
        }
        return 1
    }

    fun getGroup():

}

class Group(
    private var Name: String,
    private var Priority: Int,
    private var JoinMessage: String,
    private var ExitMessage: String,
    private val member: List<OfflinePlayer> = emptyList<OfflinePlayer>().toMutableList()
){

    fun getName(): String = Name
    fun getPriority(): Int = Priority
    fun getJoinMessage(): String = JoinMessage
    fun getExitMessage(): String = ExitMessage

    fun setJoinMessage(msg: String){
        JoinMessage = msg
    }

    fun setExitMessage(msg: String){
        ExitMessage = msg
    }

    fun toYamlConfig(): Map<String, Any>{
        return mapOf(
            Pair("Groups.$Name.Priority", 1),
            Pair("Groups.$Name.JoinMessage", JoinMessage),
            Pair("Groups.$Name.ExitMessage", ExitMessage)
        )
    }

    //TODO("暂缓")
    fun toSQL(){

    }

    fun info(): String = """
        
    """.trimIndent()

    fun equalsName(name: String): Boolean{
        return name == Name
    }

}