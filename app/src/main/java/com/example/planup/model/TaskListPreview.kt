package com.example.planup.model

data class TaskListPreview(
    val _id : String?,
    var name : String,
    val tasks: List<TaskPreview>
)
