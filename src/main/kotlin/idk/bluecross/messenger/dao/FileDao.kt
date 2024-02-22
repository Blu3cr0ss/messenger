package idk.bluecross.messenger.dao

import idk.bluecross.messenger.repository.FileRepository
import idk.bluecross.messenger.store.entity.FileInDb
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class FileDao(
    val fileRepository: FileRepository
) {
    fun save(file: FileInDb) = fileRepository.insert(file)
    fun findById(id: ObjectId) = fileRepository.findById(id)
    fun findAll() = fileRepository.findAll()
}