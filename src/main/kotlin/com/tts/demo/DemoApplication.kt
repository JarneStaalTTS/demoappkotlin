package com.tts.demo

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.SpringApplication

@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "Demo API", version = "1.0.0"))
class DemoApplication

fun main(args: Array<String>) {
    val app = SpringApplication(DemoApplication::class.java)
    app.run(*args)
}
