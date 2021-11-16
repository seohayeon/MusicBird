import listener.MessageEvent
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent


    fun main() {
        val builder = JDABuilder.createDefault(
            "OTA4NzA4Nzc4MTgxMDEzNTM0.YY5q_w.c1XULITwancMS8uCJlzIw43Uy20",
            GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES
        )
            .addEventListeners(MessageEvent())
            .build()
    }

