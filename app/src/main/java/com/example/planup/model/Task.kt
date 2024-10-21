package com.example.planup.model

data class Task(
    val _id: String?,
    val name: String,
    var description: String,
    val data: String,
    val attributes: List<Attribute>,
    val comments: List<Comment>?
)
