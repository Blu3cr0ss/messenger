package idk.bluecross.messenger.dao

import idk.bluecross.messenger.repository.FileRepository
import idk.bluecross.messenger.store.entity.FileInDb
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class FileDao(
    val fileRepository: FileRepository,
    val mongoTemplate: MongoTemplate,
) : FileRepository by fileRepository {
    fun getFileByteArrayById(id: Any) = mongoTemplate.findDistinct(
        Query(Criteria("_id").`is`(id)),
        "byteArr",
        FileInDb::class.java,
        ByteArray::class.java
    ).first()

    override fun <S : FileInDb> save(entity: S): S {
        val saved = fileRepository.save(entity)
        return saved
    }
}