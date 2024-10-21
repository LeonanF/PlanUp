package com.example.planup.model

data class taskRequest(
    val projectId: String,
    val listId: String,
    val task: Task
)
