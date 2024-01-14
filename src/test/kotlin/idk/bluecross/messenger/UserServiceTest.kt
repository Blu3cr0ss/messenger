package idk.bluecross.messenger

import idk.bluecross.messenger.service.UserService
import idk.bluecross.messenger.store.entity.FileInDb
import idk.bluecross.messenger.store.entity.IdRefList
import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.store.entity.UserDetails
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:test-db.properties"])
class UserServiceTest(
    @Autowired var userService: UserService
) {
    @Test
    fun `test loadUserByUsername()`() {
        val username = System.currentTimeMillis().toString()

        userService.save(
            User(
                "",
                FileInDb("", byteArrayOf(1)),
                User.Status.OFFLINE,
                IdRefList(),
                UserDetails(
                    username, "Bebrik", "qwe@qwe.qwe", "now give me your password"
                )
            )
        )

        Assertions.assertNotNull(userService.loadUserByUsername(username))
    }
}