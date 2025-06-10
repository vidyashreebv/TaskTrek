package com.example.tasktrek

data class TaskItem(
    val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: String
)