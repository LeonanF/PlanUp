package com.example.planup.network

import com.example.planup.model.Project
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("tasks")
    fun fetchTasks(): Call<TaskResponse>

    @GET("projects")
    fun fetchProjects(): Call<ProjectResponse>

    @GET("userProjects")
    fun fetchUserProjects(@Query("userid") userId: String): Call<ProjectResponse>

    @POST("projects")
    fun postProject(@Body newProject: Project): Call<ResponseBody>
}