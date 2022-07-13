package application

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(
    private val messageRepo: MessageRepo
) {

    @RequestMapping("/api/greet")
    fun helloWorld(): List<Message> {
        return messageRepo.findAll()
    }

}