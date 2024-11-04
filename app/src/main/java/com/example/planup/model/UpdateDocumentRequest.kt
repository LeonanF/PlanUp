package com.example.planup.model

data class UpdateDocumentRequest(
    val projectId : String,
    val listId : String,
    val taskId : String,
    val documentId : String,
    val title : String,
    val text : String
)
