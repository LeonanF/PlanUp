package com.example.planup.model

import kotlin.collections.List

data class Comment(
    val _id: String?,
    val data: Long,
    val userId: String,
    val text: String,
    val replies: List<Comment> = mutableListOf()
)
