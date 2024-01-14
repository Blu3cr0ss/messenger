package idk.bluecross.messenger.util.event

import idk.bluecross.messenger.store.entity.IdRef
import idk.bluecross.messenger.util.annotation.CascadeSave
import idk.bluecross.messenger.util.clazz.isSubtypeOf
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils


@Component
class CascadeSaveEventListener(
    val mongoOperations: MongoOperations
) : AbstractMongoEventListener<Any>() {
    override fun onBeforeConvert(event: BeforeConvertEvent<Any>) {
        val source = event.source
        ReflectionUtils.doWithFields(
            source.javaClass
        ) { field ->
            ReflectionUtils.makeAccessible(field)
            if (
                field.isAnnotationPresent(DBRef::class.java) &&
                field.isAnnotationPresent(CascadeSave::class.java)
            ) {
                val fieldValues: List<Any> = run {
                    val v = field.get(source)
                    if (v::class.java.isSubtypeOf(Iterable::class.java)) {
                        return@run (v as Iterable<Any>).toList()
                    } else return@run listOf(v)
                }

                    .run {
                        if (this.isNotEmpty() && this[0]::class.java.isSubtypeOf(IdRef::class.java))
                            map { (it as IdRef<*>).get() }
                        else this
                    }

                    .filterNotNull()

                val fieldValuesIds = fieldValues.map {
                    it::class.java.getDeclaredField("id").apply(ReflectionUtils::makeAccessible).get(it) as ObjectId
                }
                fieldValues.forEach(mongoOperations::save)
            }
        }
    }
}

