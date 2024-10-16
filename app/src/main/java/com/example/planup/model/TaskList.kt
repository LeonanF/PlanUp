package com.example.planup.model

import kotlin.collections.List

data class TaskList(
    val _id : String?,
    val name : String,
    val tasks: List<Task>
)