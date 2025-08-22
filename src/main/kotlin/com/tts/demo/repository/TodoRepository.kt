package com.tts.demo.repository

import com.tts.demo.model.Todo
import org.jooq.Source
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class TodoRepository
    (dataSource: DataSource) : JooqRepositoryAutoincrement<, >(
    dataSource,
    TODO,
    Todo::class.java,
) {