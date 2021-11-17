package command.commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.util.*


class PingCommand : ICommand {
    override fun handle(event: MessageReceivedEvent){
        println("zzzzzz")
    }
    override val help: String? = "명령어입니다"
    override val invoke: String = "help"
}