package idk.bluecross.messenger.store.repository

import idk.bluecross.messenger.store.entity.Chat
import idk.bluecross.messenger.store.entity.Message
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : MongoRepository<Chat, ObjectId> {
}

