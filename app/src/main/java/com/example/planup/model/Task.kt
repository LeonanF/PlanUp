package com.example.planup.model

data class Task(
    val name: String,
    var description: String,
    val data: String,
    val projectId: String,
    val _id: String?,
    val attributes: List<Attribute>
)
