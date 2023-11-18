package idk.bluecross.messenger.service

import idk.bluecross.messenger.store.dao.FileDao
import idk.bluecross.messenger.store.entity.FileInDb
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class FileService(
    val fileDao: FileDao
) {
    fun save(file: FileInDb) = fileDao.save(file)

    fun findById(id: ObjectId) = fileDao.findById(id)

    fun findAll() = fileDao.findAll()
}