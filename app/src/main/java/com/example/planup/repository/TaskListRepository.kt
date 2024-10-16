package com.example.planup.repository

import android.util.Log
import com.example.planup.model.TaskListRequest
import com.example.planup.network.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskListRepository {
    private val apiService = RetrofitInstance.apiService

    fun deleteList(listId: String, callback: (Boolean) -> Unit) {
        apiService.deleteList(listId).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    Log.d("DeleteList", "Lista deletada com sucesso: ${response.body()}")
                    callback(true)
                } else {
                    Log.d("DeleteList", "Falha ao deletar a lista: ${response.errorBody()?.string()}")
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

    fun postProjectList(list:TaskListRequest){
        apiService.postList(list).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful){
                    Log.d("PostList", "Lista criada com sucesso: ${response.body()}")
                } else{
                    Log.e("PostList", "Falha ao criar lista: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("PostList", "Erro ao enviar a lista: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}
