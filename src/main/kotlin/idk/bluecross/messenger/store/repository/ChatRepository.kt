package idk.bluecross.messenger.store.repository

import idk.bluecross.messenger.store.entity.Chat
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : MongoRepository<Chat, ObjectId>

