package command.commands

import command.commands.ICommand
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.util.*
import audio.GuildMusicManager
import audio.PlayerManager
import command.CommandManager
import io.github.cdimascio.dotenv.dotenv
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
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
var musiclist:List<SearchDTO> = arrayListOf()
var select:Boolean = false

class PlayCommand : ICommand {
    
    
    fun getSelect():Boolean{
        return select
    }
    fun getMusicList():List<SearchDTO>{
        return musiclist
    }
    fun setSelect(ctx:Boolean){
        select = ctx
    }
    
    
    
    override fun handle(event: MessageReceivedEvent){
            val msg: Message = event.getMessage()
            val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            var server = retrofit.create(SearchAPI::class.java)
            val channel: TextChannel = event.getTextChannel()
            val command = msg.getContentRaw()
            val voiceChannel  = event.getMember()?.getVoiceState()?.getChannel()
            val audioManager = event.getGuild().getAudioManager()
            
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
                        .queue()
                    musiclist=movieArr
                    select=true
                }
                override fun onFailure(call: Call<SearchResult>?, t: Throwable?) {
                    println(t)
                }
                
            })
            println(select)
    }
    override val help: String? = "음악 재생명령어"
    override val invoke: String = "play"
}