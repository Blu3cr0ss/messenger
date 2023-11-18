package idk.bluecross.messenger

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.time.Instant

class TestEntity {
    @Id var id = ObjectId()
    var time = Instant.now()
}