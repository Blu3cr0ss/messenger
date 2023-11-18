package idk.bluecross.messenger

import idk.bluecross.messenger.store.entity.*
import idk.bluecross.messenger.util.content.ContentTree
import idk.bluecross.messenger.util.content.GraphicContent
import idk.bluecross.messenger.util.content.TextContent
import idk.bluecross.messenger.util.db.FluxDocRef
import idk.bluecross.messenger.util.db.MonoDocRef
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux

@SpringBootTest
class ObjectReltaionsTest {
    var user1 = User(
        "user1",
        "User 1",
        "bio1",
        MonoDocRef(
            GraphicContent(MonoDocRef(FileInDb("avatar1.png", byteArrayOf(1)), FileInDb::class.java)),
            GraphicContent::class.java
        ),
        User.Status.ONLINE,
        FluxDocRef(Chat::class.java, chat_u1_u2),
        "email1@mail.ru",
        "givemeyourpassword"
    )
    var user2 = User(
        "user2",
        "User 2",
        "bio2",
        MonoDocRef(
            GraphicContent(MonoDocRef(FileInDb("avatar2.png", byteArrayOf(1)), FileInDb::class.java)),
            GraphicContent::class.java
        ),
        User.Status.ONLINE,
        FluxDocRef(Chat::class.java, chat_u1_u2),
        "email2@mail.ru",
        "mypassword?"
    )
    var chat_u1_u2 = Chat(
        FluxDocRef(
            Message::class.java,
            Message(
                sender = MonoDocRef(User::class.java, user1),
                FluxDocRef(Reaction::class.java),
                ContentTree().apply { add(TextContent("hi! this is msg from u1")) },
                Message.State.READ
            ),
            Message(
                sender = MonoDocRef(User::class.java, user2),
                FluxDocRef(Reaction::class.java),
                ContentTree().apply { add(TextContent("hi! this is msg from u2")) },
                Message.State.READ
            )
        ),
        "user1 - user2",
        "chat between 2 test users"
    )

}