package com.example.planup.model

data class Project(
    val name : String,
    val description : String?,
    val owner : String,
    val members : List<String>?,
    val taskLists : List<Task>?,
    val status : String?,
)
