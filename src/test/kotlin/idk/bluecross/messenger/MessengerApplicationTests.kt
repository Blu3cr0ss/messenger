package idk.bluecross.messenger

import idk.bluecross.messenger.store.entity.FileInDb
import idk.bluecross.messenger.util.db.FluxDocRef
import idk.bluecross.messenger.util.db.MonoDocRef
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.index.Indexed
import reactor.core.publisher.Mono
import java.time.Duration

@SpringBootTest
class MessengerApplicationTests(@Autowired val mongoOperations: ReactiveMongoOperations) {

    @Test
//    @Disabled
    fun findFileInDb() {
        MonoDocRef.findById<FileInDb>(
            ObjectId("65507fced748b11a712208e0"),
            FileInDb::class.java.name
        ).id.also(::println)
    }

    @Test
    fun monoDocRef() {
        // save entity, then get this entity by id, then check is given entity have same id
        val entity = TestEntity()
        MonoDocRef(TestEntity::class.java, entity).save().block()
        Assertions.assertEquals(
            true,
            MonoDocRef.findById(entity.id, TestEntity::class.java).id
                .map { true }
                .timeout(Duration.ofSeconds(3), Mono.just(false))
                .switchIfEmpty(Mono.just(false))
                .block()
        )
    }

    @Test
    fun fluxDocRef_save() {
        val f =
            FluxDocRef(TestEntity::class.java, TestEntity())
        f.save()
            .map { it.id }
            .doOnSubscribe { println("sub") }
            .doOnNext { println(it) }
            .doFinally { println("fin") }
            .collectList().block()!!.forEach { println("id-> " + it) }
    }

    @Test
    fun fluxDocRef() {
        // save entity, then get this entity by id, then check is given entity have same id
        val e1 = TestEntity()
        val e2 = TestEntity()

        FluxDocRef(TestEntity::class.java, e1, e2).save().blockLast()
        Assertions.assertEquals(
            true,
            FluxDocRef.findByIds(listOf(e1.id, e2.id), TestEntity::class.java).ids
                .map { true }
                .timeout(Duration.ofSeconds(3), Mono.just(false))
                .switchIfEmpty(Mono.just(false))
                .blockFirst()
        )
    }
}