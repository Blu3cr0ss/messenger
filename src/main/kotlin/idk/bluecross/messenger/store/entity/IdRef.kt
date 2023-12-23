package idk.bluecross.messenger.store.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.mongodb.DBRef
import idk.bluecross.messenger.util.ObjectIdDeserializer
import idk.bluecross.messenger.util.ObjectIdSerializer
import idk.bluecross.messenger.util.spring.Beans
import org.bson.types.ObjectId
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.util.ReflectionUtils
import java.util.Optional

@JsonIgnoreProperties("collection", "databaseName", "entity")
class IdRef<T>(
    val id: ObjectId,
    val clazz: Class<*>,
    @Transient
    val collection: String = clazz.simpleName.decapitalize(),
    @Transient
    @get:JvmName(name = "_getDatabaseName")
    @set:JvmName(name = "_setDatabaseName")
    var databaseName: String? = null
) : DBRef(databaseName, collection, id) {
    companion object {
        private val mongoOperations = Beans.getBean(MongoOperations::class.java)
        fun <T : DBEntity> fromEntity(entity: T): IdRef<T> {
            val clazz = entity::class.java as Class<T>
            val id =
                entity::class.java.getDeclaredField("id").apply(ReflectionUtils::makeAccessible).get(entity) as ObjectId
            return IdRef<T>(id, clazz).also { it.entity = entity }
        }
    }

    @Transient
    @JsonIgnore
    var entity: T? = null

    fun get(): T? {
        if (entity == null) entity = mongoOperations.findById(id, clazz, collection) as? T
        return entity!!
    }

    fun forceGet() = mongoOperations.findById(id, clazz, collection).also { entity = it as? T }
}