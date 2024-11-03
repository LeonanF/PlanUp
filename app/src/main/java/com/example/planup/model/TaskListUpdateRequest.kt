package com.example.planup.model

data class TaskListUpdateRequest(
    val projectId: String,
    val listId: String,
    val name: String
)