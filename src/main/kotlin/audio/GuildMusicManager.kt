package audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import audio.TrackScheduler
import audio.AudioPlayerSendHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer

class GuildMusicManager(manager: AudioPlayerManager) {
    /**
     * Audio player for the guild.
     */
    val player: AudioPlayer

    /**
     * Track scheduler for the player.
     */
    @JvmField
    val scheduler: TrackScheduler

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    val sendHandler: AudioPlayerSendHandler
        get() = AudioPlayerSendHandler(player)

    /**
     * Creates a player and a track scheduler.
     * @param manager Audio player manager to use for creating the player.
     */
    init {
        player = manager.createPlayer()
        scheduler = TrackScheduler(player)
        player.addListener(scheduler)
    }
}