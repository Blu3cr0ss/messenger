package idk.bluecross.messenger.api.controller

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.reflect.jvm.internal.impl.protobuf.ByteString

@RestController
@RequestMapping("/util")
class UtilController {
    @GetMapping("/bytesToString")
    fun bytesToString(@RequestParam bytes: String): ResponseEntity<*> {
        return Base64.getDecoder().runCatching {
            decode(bytes)
        }.getOrNull()?.run { ResponseEntity(this, HttpStatus.OK) } ?: ResponseEntity<Void>(HttpStatusCode.valueOf(400))
    }
}