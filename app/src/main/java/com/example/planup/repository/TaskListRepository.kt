package com.example.planup.repository

import android.util.Log
import com.example.planup.model.TaskListRequest
import com.example.planup.model.TaskList
import com.example.planup.model.TaskPreview
import com.example.planup.network.RetrofitInstance
import com.example.planup.network.TaskListResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskListRepository {
    private val apiService = RetrofitInstance.apiService

    fun deleteList(projectId: String, listId: String, callback: (Boolean) -> Unit) {
        apiService.deleteList(projectId, listId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("DeleteList", "Lista deletada com sucesso: ${response.body()}")
                    callback(true)
                } else {
                    Log.d(
                        "DeleteList",
                        "Falha ao deletar a lista: ${response.errorBody()?.string()}"
                    )
                    callback(false)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("DeleteList", "Erro ao deletar a lista: ${t.message}")
                t.printStackTrace()
                callback(false)
            }
        })
    }

    fun postProjectList(list: TaskListRequest) {
        apiService.postList(list).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    Log.d("PostList", "Lista criada com sucesso: ${response.body()}")
                } else {
                    Log.e("PostList", "Falha ao criar lista: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("PostList", "Erro ao enviar a lista: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    fun fetchProjectLists(projectId: String, callback: (TaskListResponse?, String?) -> Unit) {
        apiService.fetchLists(projectId).enqueue(object : Callback<TaskListResponse> {
            override fun onResponse(call: Call<TaskListResponse>, response: Response<TaskListResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { taskListResponse ->
                        callback(taskListResponse, null)
                        Log.d("FetchLists",  "${response.body()}")
                    }
                } else {
                    Log.e("FetchLists", "${response.errorBody()?.string()}")
                    callback(null, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<TaskListResponse>, t: Throwable) {
                callback(null, t.message)
                Log.e("FetchLists", "${t.message}")
                t.printStackTrace()
            }
        })
    }
}
