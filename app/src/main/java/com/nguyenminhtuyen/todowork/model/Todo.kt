package com.nguyenminhtuyen.todowork.model

data class Todo(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)
