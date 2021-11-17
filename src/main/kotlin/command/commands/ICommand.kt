package command.commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent


interface ICommand {
    fun handle(event: MessageReceivedEvent)
    val help: String?
    val invoke: String
}