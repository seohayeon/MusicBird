package listener
import command.commands.PlayCommand
import audio.GuildMusicManager
import audio.PlayerManager
import command.CommandManager
import io.github.cdimascio.dotenv.dotenv
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import youtube.SearchAPI
import youtube.SearchDTO
import youtube.SearchResult
import java.awt.Color


val dotenv = dotenv()
val youtube_token = dotenv["YOUTUBE_TOKEN"]

class MessageEvent: ListenerAdapter() {
    
    val manager = CommandManager()
    var selected = PlayCommand()
    
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val user: User = event.getAuthor()
        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }
    
        manager.handleCommand(event)

        val msg: Message = event.getMessage()
        val channel: TextChannel = event.getTextChannel()
        val command = msg.getContentRaw()
        val voiceChannel  = event.getMember()?.getVoiceState()?.getChannel()
        val audioManager = event.getGuild().getAudioManager()
        val regex = "^[0-5]$".toRegex()
        
        val musiclist = selected.getMusicList()
        
        if(command.matches(regex)&&selected.getSelect()){
            var match = musiclist[command.toInt()-1]
            val embed = EmbedBuilder()
                .setTitle(match.snippet?.title, null)
                .setColor(Color.red)
                .setDescription(match.snippet?.description)
                .setThumbnail(match.snippet?.thumbnails?.high?.url)
                .build()
            channel.sendMessage(embed)
                .complete()
                val videoId = match.id.videoId
                    PlayerManager.instance?.loadAndPlay(
                        channel,"https://www.youtube.com/watch?v=$videoId"
                    )
        }

    }
}