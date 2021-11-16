interface ICommand {

    fun handle(args:List<String>, event:GuildMessageReceivedEvent)

    fun getHelp(): String

    fun getInvoke(): String

}