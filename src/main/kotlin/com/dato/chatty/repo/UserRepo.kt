package com.dato.chatty.repo

import com.dato.chatty.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepo : MongoRepository<User, String> {

    fun findByEmail(email: String): Optional<User>

    //@Query("{'_id': {\$in: ?0}, friendIds: ?1}")
    fun findAllByIdInAndFriendIds(friendsIds: Set<String>, userId: String?): List<User>

}