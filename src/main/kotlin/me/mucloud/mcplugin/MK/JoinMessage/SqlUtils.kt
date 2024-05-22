package me.mucloud.mcplugin.MK.JoinMessage

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.Statement

class SQLITEConnector {

    private val CONN: Connection = HikariDataSource(HikariConfig().also {
        it.driverClassName = "org.sqlite.JDBC"
        it.jdbcUrl = "jdbc:sqlite:"
        it.isAutoCommit = true
        it.maximumPoolSize = 60
        it.minimumIdle = 20
        it.maxLifetime = 2000
    }).connection

    private val STAT: Statement = CONN.createStatement()

    fun init(){

    }

    fun addGroup(group: Group){

    }

    fun delGroup(group: String){

    }

}

internal class SQLSCRIPTS{

    fun initPlugin(): String =
        "CREATE TABLE CONF(" +
        "KEY TEXT NOT NULL," +
        "VAL BLOB NOT NULL);"

    fun initGroup(): String =
        "CREATE TABLE GROUP" +
        "(ID INT PRIMARY KEY NOT NULL," +
        "NAME TEXT NOT NULL," +
        "MODE TEXT NOT NULL," +
        "JOINMESSAGE TEXT NOT NULL," +
        "EXITMESSAGE TEXT NOT NULL);"

    fun initUser(): String =
        "CREATE TABLE USER" +
        "(ID INT PRIMARY KEY NOT NULL," +
        "NAME TEXT NOT NULL," +
        "UUID TEXT NOT NULL," +
        "GID INT NOT NULL);"

    fun writeConfig(key: String, value: String): String =
        "INSERT INTO CONF(KEY, VAL)" +
                "VALUES($key, $value);"

    fun getConfig(key: String): String =
        "SELECT VAL FROM CONF WHERE KEY = '$key'"

    fun createGroup(group: Group): String =
        "INSERT INTO GROUP(NAME, MODE, JOINMESSAGE, EXITMESSAGE)" +
        "VALUES('${group.getName()}','${group.getMode()}','${group.getJoinMessage()}','${group.getExitMessage()}');"

    fun delGroup(name: Group): String =
        "DELETE FROM GROUP WHERE (SELECT ID FROM GROUP WHERE NAME = '$name');" +
        "DELETE FROM GROUP WHERE NAME = '$name';"
    

}