package com.example.planup.repository

import android.util.Log
import com.example.planup.model.MemberRequest
import com.example.planup.model.Project
import com.example.planup.model.ProjectPreview
import com.example.planup.network.ProjectPreviewResponse
import com.example.planup.network.ProjectResponse
import com.example.planup.network.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectRepository{

    private val apiService = RetrofitInstance.apiService

    fun fetchProjectPreviews(userId: String, callback: (List<ProjectPreview>?, String?) -> Unit) {
        apiService.fetchProjectPreviews(userId).enqueue(object : Callback<ProjectPreviewResponse> {
            override fun onResponse(call: Call<ProjectPreviewResponse>, response: Response<ProjectPreviewResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.data, null)
                } else {
                    callback(null, "Erro: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ProjectPreviewResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    private val projectPreviews = mutableListOf<ProjectPreview>()

    fun addProjectPreview(project: Project) {
        projectPreviews.add(
            ProjectPreview(
                project._id,
                project.name,
                project.description,
                project.status
            )
        )
    }

    fun fetchUserProjects(userId: String, callback: (List<Project>?, String?) -> Unit) {

        apiService.fetchUserProjects(userId).enqueue(object : Callback<ProjectResponse> {

            override fun onResponse(call: Call<ProjectResponse>, response: Response<ProjectResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.data, null)
                } else {
                    callback(null, "Erro: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                callback(null, t.message)
            }

        })
    }

    fun postProject(project : Project){

        apiService.postProject(project).enqueue(object : Callback<ResponseBody>{

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if(response.isSuccessful){
                    Log.d("PostProject", "Projeto criado com sucesso: ${response.body()}")
                    addProjectPreview(project)
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

    fun postMember(memberReq : MemberRequest){
        apiService.postMember(memberReq).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    Log.d("AddMember", "Membro adicionado com sucesso: ${response.body()}")
                } else{
                    Log.e("AddMember", "Falha ao adicionar membro: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("AddMember", "Erro ao enviar membro: ${t.message}")
                t.printStackTrace()
            }

        })
    }

}