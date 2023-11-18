package idk.bluecross.messenger.util.event

import idk.bluecross.messenger.util.annotation.CascadeSave
import idk.bluecross.messenger.util.db.AbstractDocRef
import idk.bluecross.messenger.util.db.FluxDocRef
import idk.bluecross.messenger.util.db.MonoDocRef
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import reactor.core.CorePublisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.reflect.Field


@Component
class CascadeSaveEventListener(
    val mongoOperations: ReactiveMongoOperations
) : AbstractMongoEventListener<Any>() {
    override fun onBeforeConvert(event: BeforeConvertEvent<Any>) {
        val source = event.source
        ReflectionUtils.doWithFields(
            source.javaClass
        ) { field ->
            ReflectionUtils.makeAccessible(field)
            if (
                field.type.isAssignableFrom(AbstractDocRef::class.java) &&
                field.isAnnotationPresent(CascadeSave::class.java)
            ) {
                if (field.type.isAssignableFrom(MonoDocRef::class.java)) {
                    (field.get(source) as MonoDocRef<*>).`object`.subscribe {
                        mongoOperations.insert(it).subscribe {
                            println("saved $it")
                        }
                    }
                } else if (field.type.isAssignableFrom(FluxDocRef::class.java)) {
                    (field.get(source) as FluxDocRef<*>).objects.subscribe {
                        mongoOperations.insert(it).subscribe {
                            println("saved $it")
                        }
                    }
                }
            }
        }
    }
}

