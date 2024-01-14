package idk.bluecross.messenger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker

@SpringBootApplication
@EnableWebSocketMessageBroker
class MessengerApplication {
    @Autowired
    fun setAppCtx(appCtx: ApplicationContext) {
        MessengerApplication.appCtx = appCtx
    }

    companion object {
        lateinit var appCtx: ApplicationContext
    }
}

fun main(args: Array<String>) {
    runApplication<MessengerApplication>(*args)
}