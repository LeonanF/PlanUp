package com.example.planup.model

data class Subtask(
    val _id: String?,
    val name: String,
    var status: SubtaskStatus,
    val dueDate : String
)