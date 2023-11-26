package idk.bluecross.messenger

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document
class TestEntity {
    @Id var id = ObjectId()
    var time = Instant.now()
}