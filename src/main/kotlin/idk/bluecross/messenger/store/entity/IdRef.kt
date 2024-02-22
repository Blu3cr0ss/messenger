package idk.bluecross.messenger.store.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mongodb.DBRef
import idk.bluecross.messenger.util.clazz.getClassFromCollectionName
import idk.bluecross.messenger.util.spring.Beans
import org.bson.types.ObjectId
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.util.ReflectionUtils
import java.util.*

@JsonIgnoreProperties("collection", "databaseName", "entity")
class IdRef<T>(
    val id: ObjectId,
    val clazz: Class<T>,
    val collection: String = clazz.simpleName.replaceFirstChar { it.lowercase(Locale.getDefault()) },
//    val collection: String = Beans.getBean(MongoTemplate::class.java).getCollectionName(clazz),
    var database: String? = null
) : DBRef(database, collection, id) {
    companion object {
        private val mongoOperations = Beans.getBean(MongoOperations::class.java)
        fun <T : DBEntity> fromEntity(entity: T): IdRef<T> {
            val clazz = entity::class.java as Class<T>
            val id =
                entity::class.java.getDeclaredField("id").apply(ReflectionUtils::makeAccessible).get(entity) as ObjectId
            return IdRef(id, clazz).also { it.entity = entity }
        }

        fun fromDBRef(ref: DBRef): IdRef<*> {
            val clazz = getClassFromCollectionName(ref.collectionName)!!
            val id = ref.id as? ObjectId ?: ObjectId(ref.id.toString())
            return IdRef(id, clazz)
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
    override fun toString(): String = "{id=$id, class=$clazz, collection=$collection, database=$databaseName}"
}

class IdRefList<T>() : ArrayList<IdRef<T>>() {
    constructor(vararg items: IdRef<T>) : this() {
        addAll(items)
    }

    constructor(items: Collection<IdRef<T>>) : this() {
        addAll(items)
    }
}