package com.tts.demo.service

import com.tts.demo.model.Todo

interface TodoRepository {
    fun getAllTodos(): List<Todo>
}
