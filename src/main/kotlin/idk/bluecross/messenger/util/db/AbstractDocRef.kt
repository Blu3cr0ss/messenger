package idk.bluecross.messenger.util.db

import idk.bluecross.messenger.util.spring.Beans
import org.bson.types.ObjectId
import org.reactivestreams.Publisher
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Mono
import java.lang.RuntimeException

abstract class AbstractDocRef<T>(@Transient var typeClass: Class<T>) {
    var type: String = typeClass.name

    abstract fun save(): Publisher<T>
    abstract fun get(): Publisher<T>

    protected class ReferenceNotSatisfiedException(id: ObjectId, type: String) :
        RuntimeException("Cannot find object for type $type with id ${id.toHexString()}")

    protected class IdNotFoundException(type: String) :
        RuntimeException("Cannot find id field for type $type")

    protected class DbRequestTimeoutException(query: Query) :
        RuntimeException("Timeout for request for query $query")

    protected class DbReturnedEmptyResponseException(query: Query) :
        RuntimeException("DB returned empty response for query $query")

    companion object {
        @JvmStatic
        @org.springframework.data.annotation.Transient
        protected val mongoOperations = Beans.getBean(ReactiveMongoOperations::class.java)
    }
}