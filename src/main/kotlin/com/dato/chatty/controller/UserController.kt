package com.dato.chatty.controller

import com.dato.chatty.model.User
import com.dato.chatty.repo.UserRepo
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userRepo: UserRepo
) {

    @RequestMapping("/")
    fun helloWorld(): List<User> {
        return userRepo.findAll()
    }

    @RequestMapping("/private")
    fun private(): String {
        return "Private"
    }

}