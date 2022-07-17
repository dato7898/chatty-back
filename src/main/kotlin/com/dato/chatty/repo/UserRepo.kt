package com.dato.chatty.repo

import com.dato.chatty.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*
import java.util.regex.Pattern

interface UserRepo : MongoRepository<User, String> {

    fun findByEmail(email: String): Optional<User>

    //@Query("{'_id': {\$in: ?0}, friendIds: ?1}")
    fun findAllByIdInAndFriendIds(friendsIds: Set<String>, userId: String?, page: Pageable): List<User>

    @Query("{ \$or: [ { email: { \$in: ?0 } }, { firstname: { \$in: ?0 } }, { lastname: { \$in: ?0 } } ] }")
    fun findUsers(search: Set<Pattern>, page: Pageable): List<User>

}