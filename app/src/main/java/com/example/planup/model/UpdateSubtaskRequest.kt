package com.example.planup.model

data class UpdateSubtaskRequest(
    val projectId: String,
    val listId: String,
    val taskId: String,
    val subtaskId: String,
    val name: String,
    val dueDate: String
)