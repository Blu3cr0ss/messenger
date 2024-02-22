package idk.bluecross.messenger.repository

import idk.bluecross.messenger.store.entity.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ExistsQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : MongoRepository<User, ObjectId> {
    @Query("{'userDetails.username': ?0}")
    fun findByUsername(username: String): Optional<User>
    @Query("{'userDetails.email': ?0}")
    fun findByEmail(email: String): Optional<User>

    @ExistsQuery("{'userDetails.username': ?0}")
    fun existsByUsername(username: String): Boolean
    @ExistsQuery("{'userDetails.email': ?0}")
    fun existsByEmail(email: String): Boolean
}