package application

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class Controller(
    restTemplateBuilder: RestTemplateBuilder
) {

    private val restTemplate = restTemplateBuilder.build()

    @RequestMapping("/api/greet")
    fun helloWorld(): String? {
        return restTemplate.getForObject(URI("https://jsonplaceholder.typicode.com/posts"), String::class.java)
    }

}