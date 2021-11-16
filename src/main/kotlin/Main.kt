import listener.MessageEvent
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv()
val token = dotenv["TOKEN"]

    fun main() {
        println("hello")
        val builder = JDABuilder.createDefault(
            token,
            GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES
        )
            .addEventListeners(MessageEvent())
            .build()
    }

