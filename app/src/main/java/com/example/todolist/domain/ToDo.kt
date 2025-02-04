package com.example.todolist.domain

data class ToDo(
    val id: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
)

// Fake Objects
val todo1 = ToDo(
    id = 1,
    title = "ToDo 1",
    description = "Description for ToDo 1",
    isCompleted = false,
)

val todo2 = ToDo(
    id = 2,
    title = "ToDo 2",
    description = "Description for ToDo 2",
    isCompleted = true,
)

val todo3 = ToDo(
    id = 3,
    title = "ToDo 3",
    description = "Description for ToDo 3",
    isCompleted = false,
)