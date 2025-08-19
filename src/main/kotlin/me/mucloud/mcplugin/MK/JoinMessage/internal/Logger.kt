package me.mucloud.mcplugin.MK.JoinMessage.internal

import me.mucloud.mcplugin.MK.JoinMessage.Main
import java.io.File
import java.io.FileWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.logging.Level
import java.util.logging.Logger

class Logger(main: Main){

    private val instance: Logger = Logger.getLogger(Main.Prefix(false))
    private val logFile: File = File(main.dataFolder, "muLog.txt")
    private var useFileLogger: Boolean = false

    fun toLog(lvl: MessageLevel, msg: String){
        instance.log(when(lvl){
            MessageLevel.NULL, MessageLevel.INFO, MessageLevel.FINISH -> Level.INFO
            MessageLevel.WARN -> Level.WARNING
            MessageLevel.ERR -> Level.SEVERE
        }, msg)
        if(!useFileLogger) return
        when(lvl){
            MessageLevel.NULL,
            MessageLevel.FINISH,
            MessageLevel.INFO -> {
                writeLog(MessageLevel.INFO, msg)
            }
            MessageLevel.WARN -> {
                writeLog(MessageLevel.WARN, msg)
            }
            MessageLevel.ERR -> {
                writeLog(MessageLevel.ERR, msg)
            }
        }
    }

    private fun writeLog(lvl: MessageLevel, msg: String){
        if(!useFileLogger) return

        val fw = FileWriter(logFile, StandardCharsets.UTF_8, true)
        fw.write("[${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").calendar.time}][${lvl.name}] $msg\n")
        fw.flush()
        fw.close()
    }

}