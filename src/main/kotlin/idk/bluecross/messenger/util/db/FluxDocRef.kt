package idk.bluecross.messenger.util.db

import org.bson.types.ObjectId
import org.reactivestreams.Publisher
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.FindAndReplaceOptions
import org.springframework.data.mongodb.core.query.BasicUpdate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.UpdateDefinition
import org.springframework.data.mongodb.core.query.where
import org.springframework.util.ReflectionUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.Duration

class FluxDocRef<T : Any>(
    @Transient var objects: Flux<T>,
    typeClass: Class<T>
) : AbstractDocRef<T>(typeClass) {

    constructor(type: Class<T>, vararg obj: T) : this(
        Flux.just(*obj),
        type
    )

    constructor(type: Class<T>, objs: List<T>) : this(
        Flux.fromIterable(objs),
        type
    )

    var ids: Flux<ObjectId> = objects.map {
        ReflectionUtils.findField(it::class.java, "id", ObjectId::class.java)?.also(ReflectionUtils::makeAccessible)
            ?.get(it) as? ObjectId? ?: throw IdNotFoundException(type)
    }

    override fun save() = objects.flatMap {
        mongoOperations.findAndReplace(
            Query(
                Criteria.where("_id").`is`(ids.blockLast().apply(::println))
            ),   // id is found at the moment so thread won't be blocked
            it,
        ).switchIfEmpty { mongoOperations.insert(it) }
    }

    override fun get(): Publisher<T> = objects

    companion object {

        @PersistenceCreator
        @JvmStatic
        fun <T : Any> findByIds(ids: List<ObjectId>, type: String): FluxDocRef<T> {
            val q = Query(
                Criteria().orOperator(
                    ids.map {
                        Criteria("_id").`is`(it)
                    }
                )
            )
            return FluxDocRef(
                mongoOperations.find(
                    q, Class.forName(type) as Class<T>
                )
                    .timeout(Duration.ofSeconds(3), Mono.error(DbRequestTimeoutException(q)))
//                    .switchIfEmpty(Mono.error(DbReturnedEmptyResponseException(q)))
                    .apply { subscribe() },
                Class.forName(type) as Class<T>
            )
        }

        @JvmStatic
        fun <T : Any> findByIds(id: List<ObjectId>, type: Class<T>): FluxDocRef<T> = findByIds(id, type.name)
    }
}