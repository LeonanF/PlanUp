package com.example.planup.network

import com.example.planup.model.AttributeRequest
import com.example.planup.model.MemberRequest
import com.example.planup.model.Project
import com.example.planup.model.ProjectDetailPreview
import com.example.planup.model.SubtaskRequest
import com.example.planup.model.Task
import com.example.planup.model.TaskListRequest
import com.example.planup.model.TaskRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("tasks")
    fun fetchTasks(@Query("taskId") taskId: String,@Query("listId") listId: String, @Query("projectId") projectId: String): Call<Task>

    @GET("lists")
    fun fetchLists(@Query("projectId") projectId: String): Call<TaskListResponse>

    @GET("projectPreviews")
    fun fetchProjectPreviews(@Query("userId") userId: String): Call<ProjectPreviewResponse>

    @GET("projectPreview")
    fun fetchProjectPreview(@Query("projectId") projectId: String): Call<ProjectDetailPreview>

    @GET("projectMembers")
    fun fetchMembers(@Query("projectId") projectId: String): Call<MemberResponse>

    // @GET("userProjects")
    // fun fetchUserProjects(@Query("userId") userId: String): Call<ProjectResponse>

    //@GET("comments")
    //fun fetchComment(@Query("taskId") taskId: String, @Query("projectId") userId: String, @Query("comment") status: String): Call<CommentResponse>

    @POST("projects")
    fun postProject(@Body newProject: Project): Call<ResponseBody>

    @POST("projectMember")
    fun postMember(@Body memberReq: MemberRequest): Call<ResponseBody>

    @POST("attribute")
    fun postAttribute(@Body attributeReq: AttributeRequest): Call<ResponseBody>

    @POST("tasks")
    fun postTask(@Body taskRequest: TaskRequest): Call<ResponseBody>

    @POST("tasks/move")
    suspend fun moveTask(@Body params: Map<String, String>): Call<ResponseBody>

    @POST("subtask")
    fun postSubTask(@Body subtaskReq: SubtaskRequest): Call<ResponseBody>

    //@POST("comments")
    //fun addComment(@Body comment: CommentRequest): Call<ResponseBody>

    @POST("lists")
    fun postList(@Body taskListReq: TaskListRequest): Call<ResponseBody>

    @DELETE("tasks")
    fun deleteTask(@Query("projectId") projectId: String, @Query("listId") listId: String, @Query("taskId") taskId: String): Call<ResponseBody>

    @DELETE("subtask")
    fun deleteSubtask(@Query("projectId") projectId: String, @Query("listId") listId: String, @Query("taskId") taskId: String, @Query("subtaskId") subtaskId :String) : Call<ResponseBody>

    @DELETE("projectMember")
    fun deleteMember(@Query("projectId") projectId: String, @Query("memberId") memberId:String): Call<ResponseBody>

    @DELETE("lists")
    fun deleteList(@Query("projectId") projectId: String, @Query("listId") listId: String): Call<ResponseBody>
}
