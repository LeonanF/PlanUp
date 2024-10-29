package com.example.planup.model

import kotlin.collections.List

data class Task(
    val _id: String?,
    val name: String,
    var description: String,
    val data: String,
    val attributes: List<Attribute>,
    val comments: List<Comment>,
    val subtasks: List<Subtask>
)
