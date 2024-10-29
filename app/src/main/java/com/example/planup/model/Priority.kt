package com.example.planup.model

enum class Priority {
    HIGH,
    MEDIUM,
    LOW;


    fun toDatabaseString(): String {
        return this.name
    }

    companion object {
        fun fromDatabaseString(value: String): Priority? {
            return entries.find { it.name == value }
        }
    }
}