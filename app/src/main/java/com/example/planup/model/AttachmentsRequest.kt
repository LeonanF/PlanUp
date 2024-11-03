package com.example.planup.model

data class AttachmentsRequest(
    val projectId: String,
    val listId: String,
    val taskId: String,
    val attachments: Attachments
)
