package com.example.planup.repository

import com.example.planup.network.RetrofitInstance
import com.example.planup.model.Task
import com.example.planup.network.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository {

    private val apiService = RetrofitInstance.apiService

    fun fetchTasks(callback: (Result<List<Task>>) -> Unit) {
        apiService.fetchTasks().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val tasks = response.body()?.data ?: emptyList()
                    callback(Result.success(tasks))
                } else {
                    callback(Result.failure(Throwable("Error: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}