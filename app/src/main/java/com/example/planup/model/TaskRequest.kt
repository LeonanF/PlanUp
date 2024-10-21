package com.example.planup.model

data class TaskRequest(
    val projectId: String,
    val listId: String,
    val task: Task
)
