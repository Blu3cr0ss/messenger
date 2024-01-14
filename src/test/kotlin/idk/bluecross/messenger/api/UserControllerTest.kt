package idk.bluecross.messenger.api

import idk.bluecross.messenger.authenticate
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest(
    @Autowired var mock: MockMvc,
) {
    @LocalServerPort
    var port: Int = 8080

    @Test
    fun getChats() {
        val auth = authenticate(port)
        mock.get("/api/user/getChats") {
            headers {
                setBearerAuth(auth)
            }
        }.andDo {
            print()
        }
    }
}