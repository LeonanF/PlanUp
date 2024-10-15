package com.example.planup.model

import kotlin.collections.List

data class Lists(
    val id:String? = null,
    val description:String? = null,
    val tasks:List<Task>
)
