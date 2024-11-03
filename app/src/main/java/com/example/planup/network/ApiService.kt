package com.example.planup.network

import com.example.planup.model.Attachments
import com.example.planup.model.AttachmentsRequest
import com.example.planup.model.AttributeRequest
import com.example.planup.model.CommentRequest
import com.example.planup.model.MemberRequest
import com.example.planup.model.MoveTaskRequest
import com.example.planup.model.Project
import com.example.planup.model.ProjectDetailPreview
import com.example.planup.model.ReplyRequest
import com.example.planup.model.SubtaskRequest
import com.example.planup.model.Task
import com.example.planup.model.TaskListRequest
import com.example.planup.model.TaskListUpdateRequest
import com.example.planup.model.TaskRequest
import com.example.planup.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    @GET("tasks")
    fun fetchTasks(@Query("taskId") taskId: String,@Query("listId") listId: String, @Query("projectId") projectId: String): Call<Task>

    @GET("attachments")
    fun fetchAttachments(@Query("taskId") taskId: String,@Query("listId") listId: String, @Query("projectId") projectId: String): Call<Attachments>

    @GET("comments")
    fun fetchComment(@Query("taskId") taskId: String,@Query("listId") listId: String, @Query("projectId") projectId: String/*, @Query("comment") comment: String*/): Call<CommentResponse>

    @GET("replies")
    fun fetchReplies(@Query("taskId") taskId: String,listId: String, @Query("projectId") projectId: String,@Query("commentId") commentId: String): Call<ReplyResponse>

    @GET("projectPreviews")
    fun fetchProjectPreviews(@Query("userId") userId: String): Call<ProjectPreviewResponse>

    @GET("projectPreview")
    fun fetchProjectPreview(@Query("projectId") projectId: String): Call<ProjectDetailPreview>

    @GET("projectMembers")
    fun fetchMembers(@Query("projectId") projectId: String): Call<MemberResponse>

    @GET("users")
    fun fetchUser(@Query("email") email: String): Call<User>

    @POST("projects")
    fun postProject(@Body newProject: Project): Call<ResponseBody>

    @POST("projectMember")
    fun postMember(@Body memberReq: MemberRequest): Call<ResponseBody>

    @POST("attribute")
    fun postAttribute(@Body attributeReq: AttributeRequest): Call<ResponseBody>

    @POST("attachments")
    fun postAttachments(@Body attachmentsRequest: AttachmentsRequest): Call<ResponseBody>

    @POST("tasks")
    fun postTask(@Body taskRequest: TaskRequest): Call<ResponseBody>

    @POST("moveTask")
    fun moveTask(@Body moveTaskReq: MoveTaskRequest): Call<ResponseBody>

    @POST("subtask")
    fun postSubTask(@Body subtaskReq: SubtaskRequest): Call<ResponseBody>

    @POST("comments")
    fun addComment(@Body comment: CommentRequest): Call<ResponseBody>

    @POST("replies")
    fun postReply(@Body replyRequest: ReplyRequest): Call<ResponseBody>

    @POST("lists")
    fun postList(@Body taskListReq: TaskListRequest): Call<ResponseBody>

    @POST("users")
    fun postUser(@Body newUser: User): Call<ResponseBody>

    @PUT("tasks")
    fun updateTask(@Body taskRequest: TaskRequest): Call<ResponseBody>

    @PUT("updateAttribute")
    fun updateAttribute(@Body attributeRequest: AttributeRequest): Call<ResponseBody>

    @PUT("taskStatus")
    fun updateTaskStatus(@Query("projectId") projectId: String, @Query("listId") listId: String, @Query("taskId") taskId: String, @Query("status") status: String) : Call<ResponseBody>

    @PUT("subtask")
    fun updateSubtaskStatus(@Query("projectId") projectId: String, @Query("listId") listId: String, @Query("taskId") taskId: String, @Query("subtaskId") subtaskId: String, @Query("status") status: String) : Call<ResponseBody>

    @PUT("taskPriority")
    fun updateTaskPriority(@Query("projectId") projectId: String, @Query("listId") listId: String, @Query("taskId") taskId: String, @Query("priority") priority: String) : Call<ResponseBody>

    @PUT("subtask")
    fun updateSubtask(@Body subtaskReq: SubtaskRequest) : Call<ResponseBody>

    @PUT("lists")
    fun updateTaskList(@Body taskListUpdateRequest: TaskListUpdateRequest): Call<ResponseBody>

    @DELETE("tasks")
    fun deleteTask(@Query("projectId") projectId: String, @Query("listId") listId: String, @Query("taskId") taskId: String): Call<ResponseBody>

    @DELETE("subtask")
    fun deleteSubtask(@Query("projectId") projectId: String, @Query("listId") listId: String, @Query("taskId") taskId: String, @Query("subtaskId") subtaskId :String) : Call<ResponseBody>

    @DELETE("projectMember")
    fun deleteMember(@Query("projectId") projectId: String, @Query("memberId") memberId:String): Call<ResponseBody>

    @DELETE("lists")
    fun deleteList(@Query("projectId") projectId: String, @Query("listId") listId: String): Call<ResponseBody>

}
