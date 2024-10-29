package com.example.planup.model

import kotlin.collections.List

data class Comment(
    val _id: String?,
    val data: String,
    val email: String,
    val userId: String?,
    val text: String,
    val replies: List<Comment> = listOf()
)
