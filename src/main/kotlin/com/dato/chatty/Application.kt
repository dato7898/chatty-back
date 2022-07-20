package com.dato.chatty

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.task.SimpleAsyncTaskExecutor

import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import java.io.File


@EnableAsync
@SpringBootApplication
class Application {

    @Bean
    fun taskExecutor(): SimpleAsyncTaskExecutor {
        return SimpleAsyncTaskExecutor()
    }

}

fun main(args: Array<String>) {
    println(File("./").absolutePath)
    runApplication<Application>(*args)
}