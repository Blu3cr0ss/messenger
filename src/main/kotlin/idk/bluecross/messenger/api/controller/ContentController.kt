package idk.bluecross.messenger.api.controller

import idk.bluecross.messenger.service.FileService
import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.FileInDb
import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.util.content.GraphicContent
import idk.bluecross.messenger.util.content.TextContent
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.io.File

@RestController
@RequestMapping("/test/content")
class ContentController(
    val fileService: FileService,
    val userService: UserService,
    val mongoOperations: MongoOperations
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

    @PostMapping("testUser/{id}")
    fun testUser(@PathVariable id: String) = userService.find(ObjectId(id))

    var u = User(
        "bluecross",
        "Bluecross",
        "no bio",
        FileInDb("bluecross_avatar", byteArrayOf(1)),
        User.Status.OFFLINE,
        arrayListOf(),
        "",
        ""
    )
    val chat = Chat(
        arrayListOf(),
        "chat1",
        "no desc",
        arrayListOf(u)
    )

    @PostMapping("saveWithRelations")
    fun saveWithRelations() = mongoOperations.save(
        u.apply { chats.add(chat) }
    )
}
