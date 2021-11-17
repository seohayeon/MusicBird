package command.commands

import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.util.*
import audio.GuildMusicManager
import audio.PlayerManager

class PauseCommand : ICommand {
    override fun handle(event: MessageReceivedEvent){
        val channel: TextChannel = event.getTextChannel()
        
        val musicManager:GuildMusicManager? = PlayerManager.instance?.getMusicManager(channel.getGuild())
        
            musicManager?.scheduler?.pauseTrack()
    }
    override val help: String? = "현재 재생중인 음악을 일시정지합니다."
    override val invoke: String = "pause"
}