package audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import java.util.concurrent.BlockingQueue
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler(private val player: AudioPlayer) : AudioEventAdapter() {
    private val queue: BlockingQueue<AudioTrack>

    fun queue(track: AudioTrack) {
        if (!player.startTrack(track, true)) {
            queue.offer(track)
        }
    }

    fun nextTrack() {
        player.startTrack(queue.poll(), false)
    }
    
    fun pauseTrack() {
        val isPause:Boolean = player.isPaused()
        if(isPause) return
        player.setPaused(true)
    }
    
    fun resumeTrack() {
        val isPause:Boolean = player.isPaused()
        if(!isPause) return
        player.setPaused(false)
        println(isPause)
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext) {
            nextTrack()
        }
    }

    init {
        queue = LinkedBlockingQueue()
    }
}