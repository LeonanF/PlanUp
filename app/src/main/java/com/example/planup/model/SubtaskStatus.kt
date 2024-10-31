package com.example.planup.model

enum class SubtaskStatus {
    TODO,
    DOING,
    DONE;


    fun toDatabaseString(): String {
        return this.name
    }

    companion object {
        fun fromDatabaseString(value: String): SubtaskStatus? {
            return entries.find { it.name == value }
        }
    }
}