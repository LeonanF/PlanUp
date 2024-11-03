package com.example.planup.model

import kotlin.collections.List

data class Task(
    val _id: String?,
    val name: String,
    var description: String,
    val data: String,
    val priority: Priority?,
    val status: TaskStatus,
    val attributes: List<Attribute> = listOf(),
    val comments: List<Comment> = listOf(),
    val subtasks: List<Subtask> = listOf()
){

    fun validateCompletedSubtasks(): Boolean{
        if(subtasks.isNotEmpty()){
            for (subtask in subtasks) {
                if(subtask.status!=SubtaskStatus.DONE)
                    return false
            }
        }
        return true
    }

}
