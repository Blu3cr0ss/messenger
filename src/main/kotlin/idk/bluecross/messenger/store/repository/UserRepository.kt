package idk.bluecross.messenger.store.repository

import idk.bluecross.messenger.store.entity.User
import idk.bluecross.messenger.store.entity.UserDetails
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ExistsQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

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