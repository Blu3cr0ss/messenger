package idk.bluecross.messenger.util

import com.mongodb.DBRef
import idk.bluecross.messenger.store.entity.IdRef
import idk.bluecross.messenger.store.entity.IdRefList
import idk.bluecross.messenger.util.clazz.isSubtypeOf
import org.bson.Document
import org.bson.conversions.Bson
import org.springframework.data.mongodb.core.convert.*
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty
import org.springframework.data.mongodb.util.BsonUtils

class IdRefDBRefResolver(var defaultDBRefResolver: DbRefResolver) : DbRefResolver {
    override fun resolveReference(
        property: MongoPersistentProperty,
        source: Any,
        referenceLookupDelegate: ReferenceLookupDelegate,
        entityReader: ReferenceResolver.MongoEntityReader
    ): Any? = defaultDBRefResolver.resolveReference(property, source, referenceLookupDelegate, entityReader)

    override fun resolveDbRef(
        property: MongoPersistentProperty,
        dbref: DBRef?,
        callback: DbRefResolverCallback,
        proxyHandler: DbRefProxyHandler
    ): Any? {
        return if (property.rawType.isSubtypeOf(IdRef::class.java)) {
            IdRefUtils.resolveIdRef(property, callback)
        } else if (property.rawType.isSubtypeOf(IdRefList::class.java)) {
            IdRefUtils.resolveIdRefList(property, callback)
        } else defaultDBRefResolver.resolveDbRef(property, dbref, callback, proxyHandler)
    }

    private object IdRefUtils {
        fun resolveIdRefList(property: MongoPersistentProperty, callback: DbRefResolverCallback): IdRefList<*> {
            val document = (callback::class.java.getDeclaredField("surroundingObject").apply {
                isAccessible = true
            }.get(callback) as Document)
            val value = BsonUtils.resolveValue(document as Bson, property.name) as List<DBRef>
            return IdRefList<Any>().apply {
                addAll(value.map { IdRef.fromDBRef(it) as IdRef<Any> })
            }
        }

        fun resolveIdRef(property: MongoPersistentProperty, callback: DbRefResolverCallback): IdRef<*> {
            val document =
                (callback::class.java.getDeclaredField("surroundingObject").apply {  // DefaultDbRefResolverCallback
                    isAccessible = true
                }.get(callback) as Document)
            val value = BsonUtils.resolveValue(document as Bson, property.name) as DBRef
            return IdRef.fromDBRef(value)
        }
    }

    override fun fetch(dbRef: DBRef): Document? = defaultDBRefResolver.fetch(dbRef)

    override fun bulkFetch(dbRefs: MutableList<DBRef>): MutableList<Document> = defaultDBRefResolver.bulkFetch(dbRefs)

}