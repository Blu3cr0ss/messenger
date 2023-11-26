package idk.bluecross.messenger

import idk.bluecross.messenger.util.annotation.CascadeSave
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.test.context.TestPropertySource
import java.time.Instant


@SpringBootTest
@TestPropertySource(locations = ["classpath:test-db.properties"])
class CascadeSaveTest(
    @Autowired val mongoOperations: MongoOperations
) {

    @Document
    class CascadeSaveTestEntity(
        @DBRef
        @CascadeSave
        var testEntity: TestEntity
    ) {
        @Id
        var id = ObjectId()
    }

    @Document
    class CascadeSaveIterableTestEntity(
        @DBRef
        @CascadeSave
        var testEntity: List<TestEntity>
    ) {
        @Id
        var id = ObjectId()
    }

    @Test
    fun saveAndReplace() {
        val obj = CascadeSaveTestEntity(TestEntity())
        mongoOperations.save(obj)
        obj.testEntity.time = Instant.EPOCH
        mongoOperations.save(obj)
    }

    @Test
    fun saveAndReplaceIterable() {
        val obj = CascadeSaveIterableTestEntity(listOf(TestEntity(), TestEntity()))
        mongoOperations.save(obj)
        obj.testEntity.forEach { it.time = Instant.EPOCH }
        mongoOperations.save(obj)
    }
}