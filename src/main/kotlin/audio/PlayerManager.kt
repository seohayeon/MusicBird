package audio

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel

class PlayerManager {
    private val musicManagers: MutableMap<Long, GuildMusicManager>
    private val audioPlayerManager: AudioPlayerManager
    fun getMusicManager(guild: Guild): GuildMusicManager {
        return musicManagers.computeIfAbsent(guild.idLong) { guildId: Long? ->
            val guildMusicManager = GuildMusicManager(audioPlayerManager)
            guild.audioManager.sendingHandler = guildMusicManager.sendHandler
            guildMusicManager
        }
    }

    fun loadAndPlay(channel: TextChannel, trackUrl: String?) {
        val musicManager = getMusicManager(channel.guild)
        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                musicManager.scheduler.queue(track)
                channel.sendMessage("Adding to queue: `")
                    .append(track.info.title)
                    .append("` by `")
                    .append(track.info.author)
                    .append('`')
                    .queue()
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                //
            }

            override fun noMatches() {
                //
            }

            override fun loadFailed(exception: FriendlyException) {
                //
            }
        })
    }

    companion object {
        private var INSTANCE: PlayerManager? = null
        val instance: PlayerManager?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = PlayerManager()
                }
                return INSTANCE
            }
    }

    init {
        musicManagers = HashMap()
        audioPlayerManager = DefaultAudioPlayerManager()
        AudioSourceManagers.registerRemoteSources(audioPlayerManager)
        AudioSourceManagers.registerLocalSource(audioPlayerManager)
    }
}