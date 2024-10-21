package com.example.planup.repository

import android.util.Log
import com.example.planup.model.MemberRequest
import com.example.planup.model.Project
import com.example.planup.model.ProjectDetailPreview
import com.example.planup.model.ProjectPreview
import com.example.planup.network.ProjectPreviewResponse
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

    fun fetchProjectPreview(userId: String, callback: (ProjectDetailPreview?, String?) -> Unit) {

        apiService.fetchProjectPreview(userId).enqueue(object : Callback<ProjectDetailPreview> {

            override fun onResponse(call: Call<ProjectDetailPreview>, response: Response<ProjectDetailPreview>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Erro: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ProjectDetailPreview>, t: Throwable) {
                callback(null, t.message)
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