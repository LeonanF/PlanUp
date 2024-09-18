package com.example.planup.model

data class Project(
    val name : String,
    val description : String = "",
    val owner : String,
    val members : List<String> = emptyList(),
    val taskLists : List<Task> = emptyList(),
    val status : String = "",
)
