package me.duncte123.menuDocs.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;


class PingCommand: ICommand {
    
    override fun handle(args:List<String> ,event:GuildMessageReceivedEvent) {
        event.getChannel().sendMessage("Pong!").queue((message) ->
            message.editMessageFormat("Ping is %sms", event.getJDA().getPing()).queue()
        );
    }

    override fun getHelp():String {
        return "Pong!\n" +
                "Usage: `" + getInvoke() + "`";
    }

    override fun getInvoke():String {
        return "ping";
    }
}