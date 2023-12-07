package idk.bluecross.messenger

import idk.bluecross.messenger.util.spring.Beans
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
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