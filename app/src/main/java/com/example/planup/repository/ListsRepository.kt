package com.example.planup.repository

import android.util.Log
import com.example.planup.model.Lists
import com.example.planup.model.Project
import com.example.planup.network.ListResponse
import com.example.planup.network.ProjectResponse
import com.example.planup.network.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListsRepository {
    private val apiService = RetrofitInstance.apiService
    fun fetchProjectList(projectId:String, callback: (List<Lists>?, String?) -> Unit) {
        apiService.fetchProjectLists(projectId).enqueue(object : Callback<ListResponse> {
            override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.data, null)
                } else {
                    callback(null, "Erro: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                callback(null, t.message)
            }

        })
    }

    fun postProjectList(list:Lists){
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
}