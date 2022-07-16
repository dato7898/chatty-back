package com.dato.chatty.service

import com.dato.chatty.model.User
import com.dato.chatty.repo.UserRepo
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserService(
    private val userRepo: UserRepo
) {

    fun findByEmail(email: String): Optional<User> {
        return userRepo.findByEmail(email)
    }

}