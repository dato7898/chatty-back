package com.dato.chatty.controller

import com.dato.chatty.model.Greeting
import com.dato.chatty.model.HelloMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.HtmlUtils

@RestController
class UserController {

    @RequestMapping("/private")
    fun private(): String {
        return "{\"Private\":\"Private\"}"
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    fun greeting(message: HelloMessage): Greeting {
        return Greeting("Hello, " + HtmlUtils.htmlEscape(message.name) + "!")
    }

}