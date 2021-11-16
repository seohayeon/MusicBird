package audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import audio.TrackScheduler
import audio.AudioPlayerSendHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer

class GuildMusicManager(manager: AudioPlayerManager) {

    val player: AudioPlayer

    @JvmField
    val scheduler: TrackScheduler

    val sendHandler: AudioPlayerSendHandler
        get() = AudioPlayerSendHandler(player)

    init {
        player = manager.createPlayer()
        scheduler = TrackScheduler(player)
        player.addListener(scheduler)
    }
}