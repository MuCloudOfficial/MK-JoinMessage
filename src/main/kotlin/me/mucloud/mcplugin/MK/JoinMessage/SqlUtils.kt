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
        "GID TEXT NOT NULL)"

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
        STAT.executeQuery("select USER.NAME, USER.UUID, MKG.NAME from USER, MKG where USER.GID == MKG.ID").also {
            while (it.next()){
                val g = GroupManager.getGroup(it.getString("MKG.NAME")) ?: GroupManager.DEFAULT_GROUP()
                val uid = UUID.fromString(it.getString("UUID"))
                var player = Bukkit.getPlayer(uid)
                if(player == null){
                    player = Bukkit.getOfflinePlayer(uid) as Player
                }
                g.addMember(player)
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
                var player = Bukkit.getPlayer(uid)
                if(player == null){
                    player = Bukkit.getOfflinePlayer(uid) as Player
                }
                GroupManager.addSpyPlayer(player)
            }
        }
    }

    fun flushAll(){
        flushConf()
        flushGroupManager()
    }

    fun flushConf(){
        DB_initConf()
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
        val ptrGroup = "insert into MKG(NAME, MODE, SOUND, JOINMESSAGE, EXITMESSAGE) VALUES(?,?,?,?,?)"
        GroupManager.POOL().forEach {
            PS = CONN.prepareStatement(ptrGroup)
            PS.setString(1, it.getName())
            PS.setString(2, it.getMode().name)
            PS.setString(3, it.getSound().name)
            PS.setString(4, it.getJoinMessage())
            PS.setString(5, it.getExitMessage())
        }
    }

    fun unInit(){
        PS.close()
        CONN.close()
        MessageSender.sendToConsole(MessageLevel.FINISH, "已卸载 Configuration 模块")
    }

}