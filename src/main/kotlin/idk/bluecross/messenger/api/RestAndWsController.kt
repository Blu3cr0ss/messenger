package idk.bluecross.messenger.api

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/q")
@MessageMapping("/q")
class RestAndWsController {
    @GetMapping("/rest")
    fun rest(): String = "rest"

    @MessageMapping("/ws")
    @SendTo("/topic/ws")
    fun ws(): String = "ws"
}