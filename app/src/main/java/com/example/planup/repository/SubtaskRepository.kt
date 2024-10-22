package com.example.planup.repository

import android.util.Log
import com.example.planup.model.SubtaskRequest
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

}