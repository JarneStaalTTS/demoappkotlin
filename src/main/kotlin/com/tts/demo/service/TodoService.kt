package com.tts.demo.service

import com.tts.demo.model.Todo

interface TodoService {
    fun getAllTodos(): List<Todo>
}
