package com.dato.chatty

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.EnableAsync


@EnableAsync
@SpringBootApplication
class Application {

    @Bean
    fun taskExecutor(): SimpleAsyncTaskExecutor {
        return SimpleAsyncTaskExecutor()
    }

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}