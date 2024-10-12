package com.example.planup.model

data class CommentRequest(
    val projectId: String,
    val taskId: String,
    val comment: Comment
)
