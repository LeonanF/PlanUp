package com.example.planup.repository

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.planup.model.Project
import com.example.planup.network.ProjectResponse
import com.example.planup.network.RetrofitInstance
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectRepository{

    private val apiService = RetrofitInstance.apiService

    fun fetchProjects(callback: (Result<List<Project>>) -> Unit) {

        apiService.fetchProjects().enqueue(object : Callback<ProjectResponse> {

            override fun onResponse(call: Call<ProjectResponse>, response: Response<ProjectResponse>) {
                if(response.isSuccessful){
                    val projects = response.body()?.data ?: emptyList()
                    callback(Result.success(projects))
                } else{
                    callback(Result.failure(Throwable("Error: ${response.message()}")))
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                callback(Result.failure(t))
            }

        })
    }

    fun fetchUserProjects(userId: String, onSuccess: (ProjectResponse) -> Unit, onFailure: (Throwable)->Unit ){
        apiService.fetchUserProjects(userId).enqueue(object : Callback<ProjectResponse>{
            override fun onResponse(call: Call<ProjectResponse>, response: Response<ProjectResponse>) {
                if(response.isSuccessful){
                    response.body()?.let{onSuccess(it)}
                } else{
                    onFailure(Throwable("Erro: ${response.body()}"))
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    fun postProject(project : Project){

        apiService.postProject(project).enqueue(object : Callback<ResponseBody>{

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful){
                    Log.d("PostProject", "Projeto criado com sucesso: ${response.body()}")
                } else{
                    Log.e("PostProject", "Falha ao criar projeto: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("PostProject", "Erro ao enviar o projeto: ${t.message}")
                t.printStackTrace()
            }

        })

    }

}