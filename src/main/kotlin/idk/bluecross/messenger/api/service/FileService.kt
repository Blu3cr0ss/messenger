package idk.bluecross.messenger.api.service

import idk.bluecross.messenger.store.dao.FileDao
import idk.bluecross.messenger.store.entity.FileInDb
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class FileService(
    val fileDao: FileDao
) {
    fun save(file: FileInDb) = fileDao.save(file)

    fun findById(id: ObjectId) = fileDao.findById(id).getOrNull()

    fun findAll() = fileDao.findAll()
}