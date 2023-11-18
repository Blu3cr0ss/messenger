package idk.bluecross.messenger.api.controller

import idk.bluecross.messenger.service.FileService
import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.store.entity.*
import idk.bluecross.messenger.util.content.GraphicContent
import idk.bluecross.messenger.util.content.TextContent
import idk.bluecross.messenger.util.db.FluxDocRef
import idk.bluecross.messenger.util.db.MonoDocRef
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.io.File

@RestController
@RequestMapping("/test/content")
class ContentController(
    val fileService: FileService,
    val userService: UserService,
    val mongoOperations: ReactiveMongoOperations
) {
    @GetMapping("/text")
    fun text(
        @RequestParam(
            defaultValue = "Hello! This is test of TextContent(). Try adding 'text' request param"
        ) text: String
    ) = Mono.just(TextContent(text))

    @GetMapping("/file")
    fun file(@RequestParam id: String) = fileService.findById(ObjectId(id))

    @GetMapping("/files")
    fun allFiles() = fileService.findAll()

    @PostMapping("saveFile")
    fun saveFile() =
        fileService.save(
            FileInDb(
                "build.gradle",
                File("/home/andrew/IdeaProjects/messenger/build.gradle").readText().toByteArray()
            )
        )

    @PostMapping("saveUser")
    fun saveUser() = userService.save(
        User(
            avatar = MonoDocRef(
                GraphicContent::class.java,
                GraphicContent(
                    MonoDocRef(
                        FileInDb::class.java,
                        FileInDb(
                            "user1_avatar.jpg", "qwerty!".toByteArray()
                        )
                    )
                )
            ),
            bio = "test bio",
            chats = FluxDocRef(Chat::class.java),
            userName = "user1",
            displayedName = "First User",
            email = "fake@email.com",
            password = "123321",
            status = User.Status.OFFLINE,
        )
    )

    @PostMapping("testUser/{id}")
    fun testUser(@PathVariable id: String) = userService.find(ObjectId(id))
}
