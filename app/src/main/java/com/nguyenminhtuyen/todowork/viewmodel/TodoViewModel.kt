package com.nguyenminhtuyen.todowork.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.nguyenminhtuyen.todowork.model.Todo

class TodoViewModel : ViewModel() {
    private var nextId = 0
    val todos = mutableStateListOf<Todo>()
    val history = mutableStateListOf<Todo>()

    fun addTodo(title: String) {
        todos.add(Todo(id = nextId++, title = title))
    }

    fun toggleDone(id: Int) {
        val index = todos.indexOfFirst { it.id == id }
        if (index != -1) {
            val todo = todos[index]
            todos[index] = todo.copy(isDone = !todo.isDone)
            if (todo.isDone == false) {
                history.add(todo.copy(isDone = true))
            }
        }
    }
}