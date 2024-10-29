package com.example.planup.model

import kotlin.collections.List

data class Project(
    val _id : String?,
    val name : String,
    val description : String,
    val owner : String,
    val members : List<String>,
    val taskLists : List<TaskList>,
    val taskQuantity : Int = 0
)
