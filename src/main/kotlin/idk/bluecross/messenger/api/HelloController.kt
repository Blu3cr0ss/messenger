package idk.bluecross.messenger.api

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import org.springframework.web.util.HtmlUtils


@Controller
class HelloController {
    @MessageMapping("/hello")
    @SendTo("/topic/hello")
    fun greeting(payload: String): String {
        return "Hello world!"
    }

    @SubscribeMapping("/hello")
    fun subscribe(): String {
        return "subscribe to /hello"
    }
}