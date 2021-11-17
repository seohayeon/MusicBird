package command

import command.commands.ICommand
import command.commands.PingCommand
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.jetbrains.annotations.NotNull
import java.util.*


class CommandManager {
    private val commands: HashMap<String, ICommand> = HashMap()

    init {
            println("Loading commands")
            addCommand(PingCommand())
    }

    private fun addCommand(command: ICommand) {
        if (!commands.containsKey(command.invoke)) {
            commands.put(command.invoke, command)
        }
    }

    fun getCommands(): Collection<ICommand?>? {
        return commands.values
    }

    fun getCommand(@NotNull name: String?): ICommand? {
        return commands[name]
    }

    fun handleCommand(event: MessageReceivedEvent) {
        val invoke = event.getMessage().getContentRaw()
        println(invoke)
        if (commands.containsKey(invoke.split(" ")[0])) {
            event.channel.sendTyping().queue()
            commands.get(invoke)?.handle(event)
        }
    }
}