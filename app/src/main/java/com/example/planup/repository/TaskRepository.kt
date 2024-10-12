package com.example.planup.repository

import android.util.Log
import com.example.planup.model.Task
import com.example.planup.model.TaskPreview
import com.example.planup.network.RetrofitInstance
import com.example.planup.network.TaskPreviewResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class TaskRepository {

    private val apiService = RetrofitInstance.apiService

    fun deleteTask(taskId: String) {
        apiService.deleteTask(taskId).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    Log.d("DeleteTask", "Tarefa deletada com sucesso: ${response.body()}")
                } else {
                    Log.d("DeleteTask", "Falha ao deletar tarefa: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("DeleteTask", "Erro ao deletar tarefa: ${t.message}")
                    t.printStackTrace()
            }
        })
    }

    fun postTasks(task: Task) {
        apiService.postTask(task).enqueue(object : Callback <ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if (response.isSuccessful) {
                    Log.d("PostTask", "Tarefa criada com sucesso: ${response.body()}")
                    addTaskPreview(task)
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

    fun fetchTaskPreviews(projectId: String, callback: (List<TaskPreview>?, String?) -> Unit){
        apiService.fetchTaskPreviews(projectId).enqueue(object : Callback<TaskPreviewResponse>{
            override fun onResponse(
                call: Call<TaskPreviewResponse>,
                response: Response<TaskPreviewResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body()?.data, null)
                } else {
                    callback(null, "Erro: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<TaskPreviewResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    private val taskPreviews = mutableListOf<TaskPreview>()

    fun addTaskPreview(task: Task) {
        taskPreviews.add(
            TaskPreview(
                task._id,
                task.name,
                task.data
            )
        )
    }

    fun fetchTask(taskId: String, callback: (Task?, String?) -> Unit){

        apiService.fetchTasks(taskId).enqueue(object : Callback<Task>{
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful){
                    callback(response.body(), null)
                }
                else{
                    callback(null, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                callback(null, t.message)
                t.printStackTrace()
            }
        })
    }

     /*fun postComment(commentRequest: CommentRequest, callback: (Boolean, String?) -> Unit) {
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
    }*/

    /*fun getComment(commentId: String, callback: (CommentRequest?, String?) -> Unit) {
        apiService.fetchComment(commentId).enqueue(object : Callback<CommentRequest> {
            override fun onResponse(call: Call<CommentRequest>, response: Response<CommentRequest>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<CommentRequest>, t: Throwable) {
                callback(null, t.message)
                t.printStackTrace()
            }
        })
    }*/
}