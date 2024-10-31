package com.example.planup.model

data class Subtask(
    val _id: String? = null,
    val name: String,
    var status: SubtaskStatus,
    val dueDate : String
) {
    fun toApiModel(): ApiSubtask {
        return ApiSubtask(_id, name, status.toDatabaseString(), dueDate)
    }
}