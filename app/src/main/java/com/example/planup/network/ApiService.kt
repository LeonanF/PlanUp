package com.example.planup.network

import com.example.planup.model.AttributeRequest
import com.example.planup.model.MemberRequest
import com.example.planup.model.Project
import com.example.planup.model.Task
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("tasks")
    fun fetchTasks(@Query("taskId") taskId: String): Call<TaskResponse>

    @GET("userProjects")
    fun fetchUserProjects(@Query("userId") userId: String): Call<ProjectResponse>

    @POST("projects")
    fun postProject(@Body newProject: Project): Call<ResponseBody>

    @POST("projectMember")
    fun postMember(@Body memberReq: MemberRequest): Call<ResponseBody>

    @POST("attribute")
    fun postAttribute(@Body attributeReq: AttributeRequest): Call<ResponseBody>

    @POST("tasks")
    fun postTask(@Body task: Task): Call<ResponseBody>

    @DELETE("tasks")
    fun deleteTask(@Query("taskId") taskId: String): Call<ResponseBody>

}
