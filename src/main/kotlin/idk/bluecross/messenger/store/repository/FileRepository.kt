package idk.bluecross.messenger.store.repository

import idk.bluecross.messenger.store.entity.FileInDb
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : ReactiveMongoRepository<FileInDb, ObjectId> {

}