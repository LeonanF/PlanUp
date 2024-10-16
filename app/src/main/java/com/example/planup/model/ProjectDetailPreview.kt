package com.example.planup.model

data class ProjectDetailPreview(
    val _id : String?,
    val name : String,
    val description : String,
    val owner : String,
    val members : List<String>,
    val taskLists : List<TaskListPreview>,
    val status : String,
    val taskQuantity : Int = 0
)
