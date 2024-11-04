package com.example.planup.model

data class DocumentRequest(
    val projectId : String,
    val listId : String,
    val taskId : String,
    val document : Document
)