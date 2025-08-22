package com.tts.demo.controller

import com.tts.demo.service.TodoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// TodoController.kt
@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/api")
class TodoController @Autowired constructor(val todoService: TodoService) {

    @GetMapping("/")
    fun getAll(){

    }
}