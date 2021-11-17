package command

//import command.commands.ICommand
import command.commands.*
//import command.commands.PlayCommand
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.jetbrains.annotations.NotNull
import java.util.*
import java.util.regex.Pattern

class CommandManager {
    private val commands: HashMap<String, ICommand> = HashMap()

    init {
            println("Loading commands")
            addCommand(PingCommand())
            addCommand(PlayCommand())
            addCommand(PauseCommand())
            addCommand(SkipCommand())
            addCommand(ResumeCommand())
            addCommand(StopCommand())
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
        var prefix = "."
        val split: List<String> = event.getMessage().getContentRaw()
                .replaceFirst(prefix, "")
                .split("\\s+".toRegex());
        val invoke = split[0].toLowerCase()
        
        if (commands.containsKey(invoke)) {
            event.channel.sendTyping().queue()
            commands.get(invoke)?.handle(event)
        }
    }
}