package command

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent


class CommandContext(val event: GuildMessageReceivedEvent, val args: List<String>) {
    val guild: Guild
        get() = event.guild
    val getEvent: GuildMessageReceivedEvent
        get() = event
    val getArgs: List<String?>?
        get() = args

}