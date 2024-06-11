package me.mucloud.mcplugin.MK.JoinMessage

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.bukkit.Bukkit
import java.io.File
import java.sql.Connection
import java.sql.Statement
import java.util.*

internal object SQLITEConnector{

    private lateinit var CONN: Connection
    private lateinit var STAT: Statement

    fun init(main: Main){
        CONN = HikariDataSource(HikariConfig().also {
            it.driverClassName = "org.sqlite.JDBC"
            it.jdbcUrl = "jdbc:sqlite:${File(main.dataFolder, "data.db").absolutePath}"
            it.isAutoCommit = true
            it.maximumPoolSize = 10
            it.minimumIdle = 10
            it.maxLifetime = 2000
        }).connection

        STAT = CONN.createStatement()

        initStructure()
    }

    fun checkIntegrity_conf(): Boolean =
        STAT.executeQuery("SELECT sql FROM sqlite_master WHERE type = 'table' AND tbl_name = 'CONF'").next()

    fun checkIntegrity_user(): Boolean =
        STAT.executeQuery("SELECT sql FROM sqlite_master WHERE type = 'table' AND tbl_name = 'USER'").next()

    fun checkIntegrity_group(): Boolean =
        STAT.executeQuery("SELECT sql FROM sqlite_master WHERE type = 'table' AND tbl_name = 'GROUP'").next()

    fun checkIntegrity_spy(): Boolean =
        STAT.executeQuery("SELECT sql FROM sqlite_master WHERE type = 'table' AND tbl_name = 'SPY'").next()

    fun initStructure(){
        if(!checkIntegrity_conf()){
            STAT.executeUpdate("CREATE TABLE CONF(" +
                    "ID INT PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "KEY TEXT NOT NULL," +
                    "VAL BLOB NOT NULL)")
        }
        if(!checkIntegrity_group()){
            STAT.executeUpdate("CREATE TABLE \"GROUP\"(" +
                    "ID INT PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "NAME TEXT NOT NULL UNIQUE," +
                    "MODE TEXT NOT NULL," +
                    "JOINMESSAGE TEXT NOT NULL," +
                    "EXITMESSAGE TEXT NOT NULL)")
        }
        if(!checkIntegrity_user()){
            STAT.executeUpdate("CREATE TABLE USER(" +
                    "ID INT PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "NAME TEXT NOT NULL UNIQUE," +
                    "UUID TEXT NOT NULL UNIQUE," +
                    "GNAME TEXT NOT NULL)")
        }
        if(!checkIntegrity_spy()){
            STAT.executeUpdate("CREATE TABLE SPY(" +
                    "ID INT PRIMARY KEY NOT NULL," +
                    "NAME TEXT NOT NULL UNIQUE," +
                    "UUID TEXT NOT NULL UNIQUE)")
        }
    }

    fun getConf(): Map<String, String>{
        val map = mutableMapOf<String, String>()
        if(checkIntegrity_conf()){
            if(checkIntegrity_conf()){
                STAT.executeQuery("SELECT * FROM CONF").also {
                    while (it.next()){
                        map[it.getString("KEY")] = it.getString("VAL")
                    }
                }
            }
        }
    }

    fun getGroups(){
        if(checkIntegrity_group()){
            STAT.executeQuery("SELECT * FROM \"GROUP\"").also {
                if(it.next()){
                    GroupManager.addGroup(
                        Group(
                            it.getString("NAME"),
                            SendMode.match(it.getString("MODE")),
                            it.getString("JOINMESSAGE"),
                            it.getString("EXITMESSAGE")
                        )
                    )
                }
            }
            MessageSender.sendMessageToConsole(MessageLevel.FINISH, "加载了 ${GroupManager.}")
        }
    }

    fun getUsers(){
        if(checkIntegrity_user()){
            var time = 0
            STAT.executeQuery("SELECT * FROM USER").also{
                while(it.next()){
                    val target = Bukkit.getPlayer(UUID.fromString(it.getString("UUID")))
                    if(target == null){
                        MessageSender.sendMessageToConsole(MessageLevel.WARN, "存储中给定的用户 ${it.getString("NAME")} 可能不存在")
                        continue
                    }
                    time++
                    GroupManager.getGroup(it.getString("GNAME")).also { g ->
                        if(g == null){
                            MessageSender.sendMessageToConsole(MessageLevel.WARN, "${target.name} 指定的组名不在组池内，已移至默认组")
                            GroupManager.DEFAULT_GROUP.addMember(target)
                        }else{
                            g.addMember(target)
                        }
                    }
                }
            }
            MessageSender.sendMessageToConsole(MessageLevel.FINISH, "加载了 ${time.also { GroupManager.USER_SIZE = it }} 个用户")
        }
    }

    fun getSpy(){
        if(checkIntegrity_spy()){
            STAT.executeQuery("SELECT * FROM SPY").also {
                while (it.next()){
                    val target = Bukkit.getPlayer(UUID.fromString(it.getString("UUID")))
                    if(target == null){
                        MessageSender.sendMessageToConsole(MessageLevel.WARN, "存储中给定的用户 ${it.getString("NAME")} 可能不存在")
                        continue
                    }
                    GroupManager.SPY_USERS.add(target)
                }
            }
            MessageSender.sendMessageToConsole(MessageLevel.FINISH, "加载了 ${GroupManager.SPY_USERS.size} 个隐藏组成员")
        }
    }

    fun flush(){

    }

    fun close(){
        STAT.close()
        CONN.close()
    }

}