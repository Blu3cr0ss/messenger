package idk.bluecross.messenger.store.repository

import idk.bluecross.messenger.store.entity.FileInDb
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : MongoRepository<FileInDb, ObjectId>