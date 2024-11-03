package com.example.planup.model

data class MoveTaskRequest(
    val projectId: String,
    val taskId: String,
    val sourceList: String,
    val destinationList: String
)
