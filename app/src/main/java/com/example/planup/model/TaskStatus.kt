package com.example.planup.model

enum class TaskStatus {
    TODO,
    DOING,
    DONE;

    fun toDatabaseString(): String {
        return this.name
    }

    companion object {
        fun fromDatabaseString(value: String): TaskStatus? {
            return entries.find { it.name == value }
        }
    }
}