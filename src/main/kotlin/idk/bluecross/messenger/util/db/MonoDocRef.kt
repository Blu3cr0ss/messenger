package idk.bluecross.messenger.util.db

import org.bson.types.ObjectId
import org.reactivestreams.Publisher
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.FindAndReplaceOptions
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.util.ReflectionUtils
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono
import java.time.Duration

class MonoDocRef<T : Any>(
    @Transient var `object`: Mono<T>,
    typeClass: Class<T>
) : AbstractDocRef<T>(typeClass) {

    constructor(obj: T, type: Class<T>) : this(
        Mono.just(obj),
        type
    )

    constructor(type: Class<T>, obj: T) : this(
        Mono.just(obj),
        type
    )

    var id: Mono<ObjectId> = `object`.map {
        ReflectionUtils.findField(it::class.java, "id", ObjectId::class.java)?.also(ReflectionUtils::makeAccessible)
            ?.get(it) as? ObjectId? ?: throw IdNotFoundException(type)
    }

    override fun save() = `object`.flatMap {
        mongoOperations.findAndReplace(
            Query(Criteria.where("id").`is`(id.block())),   // id is found at the moment so thread won't be blocked
            it,
            FindAndReplaceOptions().upsert()
        )
//            .switchIfEmpty { mongoOperations.insert(it) }
    }

    override fun get(): Publisher<T> = `object`

    companion object {

        @PersistenceCreator
        fun <T : Any> findById(id: ObjectId, type: String): MonoDocRef<T> {
            var q = Query(Criteria.where("_id").`is`(id))
            return MonoDocRef(
                mongoOperations.find(q, Class.forName(type) as Class<T>)
                    .timeout(Duration.ofSeconds(3), Mono.error(DbRequestTimeoutException(q)))
//                    .switchIfEmpty(Mono.error(ReferenceNotSatisfiedException(id, type)))
                    .toMono()
                    .apply { subscribe() },
                Class.forName(type) as Class<T>
            )
        }

        fun <T : Any> findById(id: ObjectId, type: Class<T>): MonoDocRef<T> = findById(id, type.name)
    }
}