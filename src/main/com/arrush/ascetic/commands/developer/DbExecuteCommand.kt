package com.arrush.ascetic.commands.developer

import com.arrush.ascetic.AsceticBot
import com.arrush.ascetic.internal.command.Command
import com.arrush.ascetic.internal.command.CommandCategory
import com.arrush.ascetic.internal.command.replyAndGet
import discord4j.core.event.domain.message.MessageCreateEvent
import me.arrush.javabase.Query
import me.arrush.javabase.databases.Database
import me.arrush.javabase.entities.Row
import me.arrush.javabase.entities.Table
import org.postgresql.util.PSQLException
import reactor.core.publisher.Mono
import java.sql.ResultSet

class DbExecuteCommand : Command("dbexec", CommandCategory.DEVELOPERS, "dbexec <command>") {

    override fun onCommand(event: MessageCreateEvent, vararg args: String): Mono<Void> {
        val sqlCode = args.joinToString(separator = " ")
        val query = when {
            sqlCode.toLowerCase().contains("TABLE") -> Query<Table>(AsceticBot.database).withStatement(sqlCode)
            sqlCode.toLowerCase().contains("DATABASE") -> Query<Database>(AsceticBot.database).withStatement(sqlCode)
            else -> Query<Row> (AsceticBot.database).withStatement(sqlCode)
        }


        val rs: String = try {
            this.parseResult(query.fetchResult())
        } catch (ignored: PSQLException) {
            "No results were returned by the query"
        }

        return event.replyAndGet("**Following SQL Script has been executed successfully:**\n```fix\n$sqlCode\n```" +
                "\n**Result:**\n```fix\n$rs\n```").then()
    }

    private fun parseResult(rs: ResultSet): String {
        val list: MutableList<String> = mutableListOf()
        while (rs.next()) {
            val sb = StringBuilder()
            for (i in 0 until rs.metaData.columnCount) {
                sb.append(rs.getObject(i).toString()).append("\n")
            }
            list.add(sb.toString())
        }
        return list.joinToString(separator = "\n")
    }
}