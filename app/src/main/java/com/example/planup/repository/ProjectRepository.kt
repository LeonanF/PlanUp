package com.example.planup.repository

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.planup.model.Project
import com.example.planup.network.ProjectResponse
import com.example.planup.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    @Composable
    fun PrintProjects(){

        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                fetchProjects { result ->
                    result.onSuccess { projects ->
                        for(project in projects){
                            println(project.name+": "+ project.description)
                        }
                    }.onFailure { throwable ->
                        println(throwable.message)
                    }
                }
            }
        }

    }

    fun postProject(project : Project){

        val call = apiService.postProject(project)

        call.enqueue(object : Callback<Project>{

            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if(response.isSuccessful){
                    val createdProject = response.body()
                    Log.d("PostProject", "Projeto criado com sucesso: ${createdProject?.name}")
                } else{
                    Log.e("PostProject", "Falha ao criar projeto: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                Log.e("PostProject", "Erro ao enviar o projeto: ${t.message}")
            }

        })

    }

}