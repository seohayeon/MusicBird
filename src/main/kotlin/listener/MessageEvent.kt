package listener

import audio.GuildMusicManager
import audio.PlayerManager
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
import io.github.cdimascio.dotenv.dotenv
import youtube.SearchAPI
import youtube.SearchDTO
import youtube.SearchResult
import java.awt.Color

val dotenv = dotenv()
val youtube_token = dotenv["YOUTUBE_TOKEN"]

class MessageEvent: ListenerAdapter() {

    private val manager:CommandManager = CommandManager();
    var musiclist:List<SearchDTO> = arrayListOf()
    var select = false
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var server = retrofit.create(SearchAPI::class.java)

        val user: User = event.getAuthor()
        val msg: Message = event.getMessage()
        val channel: TextChannel = event.getTextChannel()
        val command = msg.getContentRaw()
        val voiceChannel  = event.getMember()?.getVoiceState()?.getChannel()
        val audioManager = event.getGuild().getAudioManager()
        val regex = "^[0-5]$".toRegex()

        if (command.startsWith(".play"))
        {
            if (voiceChannel == null)
            {
                channel.sendMessage("먼저 음성채널에 입장해주세요.").queue()
                return
            }
            audioManager.openAudioConnection(voiceChannel)
            
            if(command.length < 7){
                channel.sendMessage("검색어가 없습니다. '.play [노래제목]' 을 입력해주세요..").queue()
                return
            }
            var q = command.substring(6)
            server.getInfo(youtube_token,"snippet","5","KR",q).enqueue(object:
                Callback<SearchResult> {
                override fun onResponse(call: Call<SearchResult>?, response: Response<SearchResult>?) {
                    val movieResponse  = response?.body()
                    val movieArr = movieResponse?.searchList
                    if(movieArr == null) return
                    var string = ""
                    var i = 1
                    for (value in movieArr) {
                        string += "\n"+"$i | "+value?.snippet?.title
                        i++
                    }
                    channel.sendMessage("'$q' 의 검색결과입니다. 1분안에 번호를 골라주세요.\n$string")
                        .complete()
                    musiclist=movieArr
                    select=true
                }
                override fun onFailure(call: Call<SearchResult>?, t: Throwable?) {
                    println(t)
                }

            })
        }else if(command.matches(regex)&&select==true){
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
                println(
                    PlayerManager.instance?.loadAndPlay(
                        channel,"https://www.youtube.com/watch?v=$videoId"
                    )
                )

        }else if(command==".stop"){
            val musicManager:GuildMusicManager? = PlayerManager.instance?.getMusicManager(channel.getGuild())
            //musicManager?.scheduler?.stopTrack()
        }else if(command==".skip"){
            val musicManager:GuildMusicManager? = PlayerManager.instance?.getMusicManager(channel.getGuild())
            musicManager?.scheduler?.nextTrack()
        }else if(command==".pause"){
            val musicManager:GuildMusicManager? = PlayerManager.instance?.getMusicManager(channel.getGuild())
            musicManager?.scheduler?.pauseTrack()
        }else if(command==".resume"){
            val musicManager:GuildMusicManager? = PlayerManager.instance?.getMusicManager(channel.getGuild())
            musicManager?.scheduler?.resumeTrack()
        }else if(command==".prev"){
            
        }else if(command==".leave"){
            
        }

    }
}