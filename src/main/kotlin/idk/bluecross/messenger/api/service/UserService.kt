package idk.bluecross.messenger.api.service

import idk.bluecross.messenger.store.dao.UserDao
import idk.bluecross.messenger.store.entity.FileInDb
import idk.bluecross.messenger.store.entity.User
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class UserService(
    val userDao: UserDao
) {
    fun save(user: User) = userDao.save(user)
    fun find(id: ObjectId) = userDao.find(id)
}