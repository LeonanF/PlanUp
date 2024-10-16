package com.example.planup.model

data class ProjectPreview(
    val _id: String,
    val name: String,
    val description: String,
    val status: String,
    val taskQuantity : Int
)