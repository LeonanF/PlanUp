package com.example.planup.model

data class SubtaskRequest(
    val projectId: String,
    val listId : String,
    val taskId : String,
    val subtask : ApiSubtask
)
