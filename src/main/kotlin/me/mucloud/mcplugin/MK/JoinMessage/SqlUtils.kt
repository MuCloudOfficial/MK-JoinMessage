package me.mucloud.mcplugin.MK.JoinMessage

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.io.File
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement
import java.util.*

internal object SQLITEConnector{

    private lateinit var CONN: Connection
    private lateinit var PS: PreparedStatement
    private lateinit var STAT: Statement

    fun init(main: Main){
        CONN = HikariDataSource(HikariConfig().also {
            it.driverClassName = "org.sqlite.JDBC"
            it.jdbcUrl = "jdbc:sqlite:${File(main.dataFolder, "data.db").absolutePath}"
            it.isAutoCommit = true
            it.maximumPoolSize = 10
            it.minimumIdle = 10
            it.maxLifetime = 30000
        }).connection

        STAT = CONN.createStatement()
        initStructure()
        MessageSender.sendToConsole(MessageLevel.FINISH, "已加载 SQLITE CONN 模块")
    }

    private const val SQL_CONF_STRUCT =
        "CREATE TABLE CONF(" +
        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
        "KEY TEXT NOT NULL," +
        "VAL BLOB NOT NULL)"

    private const val SQL_GROUP_STRUCT =
        "CREATE TABLE MKG(" +
        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
        "NAME TEXT NOT NULL," +
        "MODE TEXT NOT NULL," +
        "SOUND TEXT NOT NULL," +
        "JOINMESSAGE TEXT NOT NULL," +
        "EXITMESSAGE TEXT NOT NULL)"

    private const val SQL_USER_STRUCT =
        "CREATE TABLE USER(" +
        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
        "NAME TEXT NOT NULL," +
        "UUID TEXT NOT NULL," +
        "GNAME TEXT NOT NULL)"

    private const val SQL_SPY_STRUCT =
        "CREATE TABLE SPY(" +
        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
        "NAME TEXT NOT NULL," +
        "UUID TEXT NOT NULL)"

    fun checkIntegrity_conf(): Boolean =
        STAT.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' AND tbl_name = 'CONF' AND sql = '$SQL_CONF_STRUCT'").next()

    fun checkIntegrity_user(): Boolean =
        STAT.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' AND tbl_name = 'USER' AND sql = '$SQL_USER_STRUCT'").next()

    fun checkIntegrity_group(): Boolean =
        STAT.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' AND tbl_name = 'MKG' AND sql = '$SQL_GROUP_STRUCT'").next()

    fun checkIntegrity_spy(): Boolean =
        STAT.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table' AND tbl_name = 'SPY' AND sql = '$SQL_SPY_STRUCT'").next()

    private fun initStructure(){
        DB_initConf()
        DB_initGroup()
        DB_initUser()
        DB_initSpy()
    }

    private fun DB_initConf(){
        if(!checkIntegrity_conf()){
            STAT.executeUpdate(SQL_CONF_STRUCT)
        }
    }

    private fun DB_initGroup(){
        if(!checkIntegrity_group()){
            STAT.executeUpdate(SQL_GROUP_STRUCT)
        }
    }

    private fun DB_initUser(){
        if(!checkIntegrity_user()){
            STAT.executeUpdate(SQL_USER_STRUCT)
        }
    }

    private fun DB_initSpy(){
        if(!checkIntegrity_spy()){
            STAT.executeUpdate(SQL_SPY_STRUCT)
        }
    }

    internal fun readConf(): Map<String, String>{
        val map = mutableMapOf<String, String>()
        if(!checkIntegrity_conf()){
            return map
        }
        STAT.executeQuery("select * from CONF").also {
            while(it.next()){
                map[it.getString("KEY")] = it.getString("VAL")
            }
        }
        return map
    }

    internal fun readGroup(): List<Group>{
        val list = mutableListOf<Group>()
        if(!checkIntegrity_group()){
            return list
        }
        STAT.executeQuery("select * from MKG").also {
            while(it.next()){
                val g = Group(
                    it.getString("NAME"),
                    SendMode.match(it.getString("MODE")),
                    Sound.valueOf(it.getString("SOUND")),
                    it.getString("JOINMESSAGE"),
                    it.getString("EXITMESSAGE"))
                list.add(g)
            }
        }
        return list
    }

    internal fun readUser(){
        if(!checkIntegrity_user()){
            return
        }
        STAT.executeQuery("select USER.NAME, USER.UUID, USER.GNAME from USER, MKG where USER.GNAME == MKG.NAME").also {
            while (it.next()){
                val g = GroupManager.getGroup(it.getString("GNAME"))
                val uid = UUID.fromString(it.getString("UUID"))
                val player = Bukkit.getOfflinePlayer(uid)
                g!!.addMember(player)
            }
        }
    }

    internal fun readSpy(){
        if(!checkIntegrity_spy()){
            return
        }
        STAT.executeQuery("select * from SPY").also {
            while(it.next()){
                val uid = UUID.fromString(it.getString("UUID"))
                val player = Bukkit.getOfflinePlayer(uid)
                GroupManager.addSpyPlayer(player)
            }
        }
    }

    fun flushConf(){
        DB_initConf()
        STAT.executeUpdate("delete from CONF")
        STAT.executeUpdate("update sqlite_sequence set seq = 0 where name = 'CONF'")
        val ptr = "insert into CONF(KEY, VAL) VALUES(?,?)"
        Configuration.getConfiguration().forEach{
            PS = CONN.prepareStatement(ptr)
            PS.setString(1, it.key)
            PS.setString(2, it.value)
            PS.executeUpdate()
        }
    }

    fun flushGroupManager(){
        DB_initGroup()
        DB_initUser()
        DB_initSpy()
        STAT.executeUpdate("delete from MKG")
        STAT.executeUpdate("delete from USER")
        STAT.executeUpdate("delete from SPY")
        STAT.executeUpdate("update sqlite_sequence set seq = 0 where name = 'MKG'")
        STAT.executeUpdate("update sqlite_sequence set seq = 0 where name = 'USER'")
        STAT.executeUpdate("update sqlite_sequence set seq = 0 where name = 'SPY'")
        val ptrGroup = "insert into MKG(NAME, MODE, SOUND, JOINMESSAGE, EXITMESSAGE) VALUES(?,?,?,?,?)"
        val ptrUser = "insert into USER(NAME, UUID, GNAME) VALUES (?,?,?)"
        GroupManager.POOL().forEach { g ->
            PS = CONN.prepareStatement(ptrGroup)
            PS.setString(1, g.getName())
            PS.setString(2, g.getMode().name)
            PS.setString(3, g.getSound().name)
            PS.setString(4, g.getJoinMessage())
            PS.setString(5, g.getExitMessage())
            PS.executeUpdate()
            g.getMembers().forEach{ p ->
                PS = CONN.prepareStatement(ptrUser)
                PS.setString(1, p.name)
                PS.setString(2, p.uniqueId.toString())
                PS.setString(3, g.getName())
                PS.executeUpdate()
            }
        }
        GroupManager.SPY().forEach {
            val ptrSpy = "insert into SPY(NAME, UUID) VALUES(?,?)"
            PS = CONN.prepareStatement(ptrSpy)
            PS.setString(1, it.name)
            PS.setString(2, it.uniqueId.toString())
            PS.executeUpdate()
        }
    }

    fun unInit(){
        PS.close()
        CONN.close()
        MessageSender.sendToConsole(MessageLevel.FINISH, "已卸载 SQLITE CONN 模块")
    }

}