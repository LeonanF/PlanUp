package com.example.planup.repository

import android.util.Log
import com.example.planup.model.SubtaskRequest
import com.example.planup.model.UpdateSubtaskRequest
import com.example.planup.network.RetrofitInstance.apiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubtaskRepository {

    fun postSubTask(subtaskRequest: SubtaskRequest) {
        apiService.postSubTask(subtaskRequest).enqueue(object : Callback <ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if (response.isSuccessful) {
                    Log.d("PostSubTask", "Subtarefa criada com sucesso: ${response.body()}")
                } else {
                    Log.d("PostSubTask", "Falha ao criar subtarefa: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable){
                Log.e("PostSubTask", "Erro ao enviar subtarefa: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    fun updateSubtaskStatus(projectId: String, listId: String, taskId: String, subtaskId: String, status: String){
        apiService.updateSubtaskStatus(projectId, listId, taskId, subtaskId, status).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    Log.d("UpdateSubtaskStatus", "Status atualizado com sucesso: ${response.body()}")
                } else{
                    Log.d("UpdateSubtaskStatus", "Falha ao atualizar status: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("UpdateSubtaskStatus", "Erro ao atualizar status: ${t.message}")
            }
        })
    }

    fun deleteSubtask(projectId: String, listId: String, taskId: String, subtaskId :String){

        apiService.deleteSubtask(projectId = projectId, listId = listId, taskId = taskId, subtaskId = subtaskId).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful){
                    Log.d("DeleteSubTask", "Subtarefa deletada com sucesso: ${response.body()}")
                } else{
                    Log.d("DeleteSubTask", "Falha ao deletar subtarefa: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("DeleteSubTask", "Erro na solicitação de deletar: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    fun updateSubtask(updateSubtaskRequest: UpdateSubtaskRequest) {
        apiService.updateSubtask(updateSubtaskRequest).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(p0: Call<ResponseBody>, p1: Response<ResponseBody>) {
                if(p1.isSuccessful){
                    Log.d("UpdateSubtask", "Subtarefa atualizada com sucesso: ${p1.body()}")
                } else {
                    Log.d("UpdateSubtask", "Falha ao atualizar subtarefa: ${p1.errorBody()?.string()}")
                }
            }

            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                Log.e("UpdateSubtask", "Erro ao atualizar subtarefa: ${p1.message}")
            }
        })
    }
}