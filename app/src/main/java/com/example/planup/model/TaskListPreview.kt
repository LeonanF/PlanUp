package com.example.planup.model

data class TaskListPreview(
    val _id : String?,
    val name : String,
    val tasks: List<TaskPreview>
)
