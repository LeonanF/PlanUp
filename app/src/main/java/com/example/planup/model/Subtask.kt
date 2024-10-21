package com.example.planup.model

data class Subtask(
    val _id: String?,
    val name: String,
    val status: Status
) {
    fun toApiModel(): ApiSubtask {
        return ApiSubtask(_id, name, status.toDatabaseString())
    }
}