package com.tts.demo.service

import com.tts.demo.model.Todo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class TodoServiceImpl : TodoService {
    @Autowired
    lateinit var todoRepository: TodoRepository
    override fun getAllTodos(): List<Todo> {

    }
}
