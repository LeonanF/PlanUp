package com.example.planup.repository

import android.util.Log
import com.example.planup.model.CommentRequest
import com.example.planup.model.Priority
import com.example.planup.model.Reply
import com.example.planup.model.ReplyRequest
import com.example.planup.model.Task
import com.example.planup.model.TaskRequest
import com.example.planup.model.TaskStatus
import com.example.planup.network.CommentResponse
import com.example.planup.network.ReplyResponse
import com.example.planup.network.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class TaskRepository {

    private val apiService = RetrofitInstance.apiService

    fun moveTask(
        projectId: String,
        taskId: String,
        destinationList: String,
        onResult: (Boolean) -> Unit,
    ) {
        val params = mapOf(
            "projectId" to projectId,
            "taskId" to taskId,
            "destinationList" to destinationList
        )

        apiService.moveTask(params).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    onResult(true)
                    Log.d("MoveTask", "Tarefa movida com sucesso: ${response.body()}")
                } else {
                    onResult(false)
                    Log.d("MoveTask", "Falha ao mover a tarefa: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                onResult(false)
                Log.e("MoveTask", "Erro ao mover a tarefa: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    fun deleteTask(projectId: String, listId: String, taskId: String, callback: (Boolean) -> Unit) {
        apiService.deleteTask(projectId, listId, taskId).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    callback(true)
                    Log.d("DeleteTask", "Tarefa deletada com sucesso: ${response.body()}")
                } else {
                    callback(false)
                    Log.d("DeleteTask", "Falha ao deletar tarefa: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(false)
                Log.e("DeleteTask", "Erro ao deletar tarefa: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    fun postTasks(taskRequest: TaskRequest) {
        apiService.postTask(taskRequest).enqueue(object : Callback <ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if (response.isSuccessful) {
                    Log.d("PostTask", "Tarefa criada com sucesso: ${response.body()}")
                } else {
                    Log.d("PostTask", "Falha ao criar tarefa: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable){
                Log.e("PostTask", "Erro ao enviar tarefa: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    fun fetchTask(taskId: String, listId: String, projectId: String, callback: (Task?, String?) -> Unit){

        apiService.fetchTasks(taskId, listId, projectId).enqueue(object : Callback<Task>{
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful){
                    callback(response.body(), null)
                    Log.d("FetchTask", "Tarefa encontrada: ${response.body()}")
                }
                else{
                    callback(null, response.errorBody()?.string())
                    Log.e("FetchTask", "Erro ao buscar tarefa: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                callback(null, t.message)
                t.printStackTrace()
            }
        })
    }

     fun postComment(commentRequest: CommentRequest, callback: (Boolean, String?) -> Unit) {
        apiService.addComment(commentRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("PostComment", "Comentário adicionado com sucesso")
                    callback(true, null)
                } else {
                    Log.d("PostComment", "Falha ao adicionar comentário: ${response.errorBody()?.string()}")
                    callback(false, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("PostComment", "Erro ao enviar comentário: ${t.message}")
                callback(false, t.message)
                t.printStackTrace()
            }
        })
    }

    fun getComments(taskId: String, listId: String, projectId: String, callback: (CommentResponse?, String?) -> Unit) {
        apiService.fetchComment(taskId, listId, projectId).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                callback(null, t.message)
                t.printStackTrace()
            }
        })
    }

    fun updateTask(taskRequest: TaskRequest) {
        apiService.updateTask(taskRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("UpdateTask", "Tarefa atualizada com sucesso: ${response.body()}")
                } else {
                    Log.d("UpdateTask", "Falha ao atualizar tarefa: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("UpdateTask", "Erro ao atualizar tarefa: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    fun postReply(replyRequest: ReplyRequest, callback: (Boolean, String?) -> Unit) {
        apiService.postReply(replyRequest).enqueue(object : Callback<ReplyResponse> {
            override fun onResponse(call: Call<ReplyResponse>, response: Response<ReplyResponse>) {
                if (response.isSuccessful) {
                    Log.d("PostReply", "Resposta adicionada com sucesso")
                    callback(true, null)
                } else {
                    Log.d("PostReply", "Falha ao adicionar resposta: ${response.errorBody()?.string()}")
                    callback(false, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<ReplyResponse>, t: Throwable) {
                Log.e("PostReply", "Erro ao enviar resposta: ${t.message}")
                callback(false, t.message)
                t.printStackTrace()
            }
        })
    }

    fun getReplies(taskId: String,listId: String, projectId: String, commentId: String, callback: (List<Reply>?, String?) -> Unit) {
        apiService.fetchReplies(taskId, commentId).enqueue(object : Callback<ReplyResponse> {
            override fun onResponse(call: Call<ReplyResponse>, response: Response<ReplyResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.data, null)
                    Log.d("FetchReplies", "Respostas encontradas: ${response.body()?.data}")
                } else {
                    callback(null, response.errorBody()?.string())
                    Log.e("FetchReplies", "Erro ao buscar respostas: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ReplyResponse>, t: Throwable) {
                callback(null, t.message)
                Log.e("FetchReplies", "Erro ao buscar respostas: ${t.message}")
                t.printStackTrace()
            }
        })
    }


    fun updateTaskStatus(projectId: String, listId: String, taskId: String, status: TaskStatus){
        apiService.updateTaskStatus(projectId, listId, taskId, status.toDatabaseString()).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    Log.d("UpdateTaskStatus", "Status atualizado com sucesso: ${response.body()}")
                } else{
                    Log.d("UpdateTaskStatus", "Falha ao atualizar status: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("UpdateTaskStatus", "Erro ao atualizar status de tarefa: ${t.message}")
                t.printStackTrace()
            }

        })
    }

    fun updateTaskPriority(projectId: String, listId: String, taskId: String, priority: Priority){
        apiService.updateTaskPriority(projectId, listId, taskId, priority.toDatabaseString()).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    Log.d("UpdateTaskPriority", "Prioridade atualizada com sucesso: ${response.body()}")
                } else{
                    Log.d("UpdateTaskPriority", "Falha ao atualizar prioridade: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("UpdateTaskPriority", "Erro ao atualizar prioridade de tarefa: ${t.message}")
                t.printStackTrace()
            }

        })
    }
    fun postMemberTask(projectId: String, listId: String, taskId: String, memberId: String) {
        apiService.postMemberTask(projectId, listId, taskId, memberId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("PostMemberTask", "Membro adicionado à tarefa com sucesso: ${response.body()}")
                } else {
                    Log.d("PostMemberTask", "Falha ao adicionar membro à tarefa: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("PostMemberTask", "Erro ao adicionar membro à tarefa: ${t.message}")
                t.printStackTrace()
            }
        })
    }

}