import commands

class CommandManager {
    private val commands: MutableMap<String, ICommand>
    init {
        commands = HashMap()
        addCommand(PingCommand())
        addCommand(HelpCommand())
    }
    
    private fun addCommand(command:ICommand) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }
    
    fun getCommands():Collection<ICommand> {
        return commands.values();
    }
    
    fun getCommand(name:String): ICommand {
        return commands.get(name);
    }
    
    fun handleCommand(event:GuildMessageReceivedEvent) {
        val split: String[]  = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(Constants.PREFIX), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            commands.get(invoke).handle(args, event);
        }
    }

}