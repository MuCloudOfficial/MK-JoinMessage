package me.mucloud.mcplugin.MK.JoinMessage.group

import me.mucloud.mcplugin.MK.JoinMessage.internal.Configuration.save
import me.mucloud.mcplugin.MK.JoinMessage.internal.SendMode
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player


object GroupManager{

    private val POOL: MutableList<Group> = mutableListOf()
    private val DEFAULT_GROUP = Group("default", SendMode.CHAT)
    private val SPY_GROUP = Group("Spy", SendMode.NULL)

    fun init(){
        POOL.add(DEFAULT_GROUP)
        POOL.add(SPY_GROUP)
    }

    fun save(){
        POOL.forEach { p ->
            p.save()
        }
    }

    // 0 = Success | 1 = Already Exist
    internal fun addGroup(group: Group): Boolean{
        val target = getGroup(group.getName())
        if(target == null){
            POOL.add(group)
            return true
        }
        return false
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

    internal fun size(): Int {
        var sum = 0
        POOL.forEach { p ->
            sum += p.size()
        }
        return sum
    }

    internal fun POOL(): List<Group> = POOL

    internal fun SPY(): Group = SPY_GROUP

}
