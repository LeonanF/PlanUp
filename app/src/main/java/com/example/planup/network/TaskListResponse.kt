package com.example.planup.network

import com.example.planup.model.TaskList

// Apagar se não precisar mais
data class TaskListResponse(
    val data: List<TaskList>
)
