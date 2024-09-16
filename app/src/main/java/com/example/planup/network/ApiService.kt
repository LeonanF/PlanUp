package com.example.planup.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("tasks")
    fun fetchTasks(): Call<ApiResponse>
}