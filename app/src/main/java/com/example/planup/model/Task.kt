package com.example.planup.model

import kotlin.collections.List

data class Task(
    val _id: String?,
    val name: String,
    var description: String,
    val data: String,
    val priority: Priority?,
    val status: TaskStatus,
    val attributes: List<Attribute> = listOf(),
    val attachments: List<Attachments> = listOf(),
    val comments: List<Comment> = listOf(),
    val subtasks: List<Subtask> = listOf()
)
