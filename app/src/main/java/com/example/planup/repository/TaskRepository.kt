package com.example.planup.repository

import android.adservices.adid.AdId
import android.util.Log
import com.example.planup.model.Project
import com.example.planup.model.Task
import com.example.planup.network.RetrofitInstance
import com.example.planup.network.TaskResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository {

    private val apiService = RetrofitInstance.apiService

    fun postTasks(task: Task) {
        apiService.postTask(task).enqueue(object : Callback <ResponseBody> {
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

    fun fetchTask(taskId: String, callback: (Task?, String?) -> Unit){

        apiService.fetchTasks(taskId).enqueue(object : Callback<TaskResponse>{
            override fun onResponse(call: Call<TaskResponse>, response: Response<TaskResponse>) {
                if (response.isSuccessful){
                    callback(response.body()?.data, null)
                }
                else{
                    callback(null, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<TaskResponse>, t: Throwable) {
                callback(null, t.message)
                t.printStackTrace()
            }
        })
    }
}