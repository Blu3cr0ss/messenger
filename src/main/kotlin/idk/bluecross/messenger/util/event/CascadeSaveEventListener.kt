package idk.bluecross.messenger.util.event

import idk.bluecross.messenger.util.annotation.CascadeSave
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils


@Component
class CascadeSaveEventListener(
    val mongoOperations: MongoOperations
) : AbstractMongoEventListener<Any>() {
    override fun onBeforeConvert(event: BeforeConvertEvent<Any>) {
        val source = event.source
        println(source::class.java.name + " -> ")
        ReflectionUtils.doWithFields(
            source.javaClass
        ) { field ->
            ReflectionUtils.makeAccessible(field)
            println("   " + field.name)
            if (
                field.isAnnotationPresent(DBRef::class.java) &&
                field.isAnnotationPresent(CascadeSave::class.java)
            ) {
                println("       annotation present on field " + field.name)
                println(field.type)
                val fieldValue = field.get(source)
                val fieldValueId =
                    fieldValue::class.java.getDeclaredField("id").apply { ReflectionUtils.makeAccessible(this) }
                        .get(fieldValue) as ObjectId
                println("           $fieldValueId")
                if (!mongoOperations.exists(
                        Query(Criteria.where("id").`is`(fieldValueId)),
                        fieldValue::class.java
                    )
                ) {
                    mongoOperations.save(
                        fieldValue
                    ).apply { print("           saved:"); println(this) }
                } else mongoOperations.findAndReplace(
                    Query(Criteria.where("_id").`is`(fieldValueId)),
                    fieldValue
                )!!

            }
        }
    }
}

