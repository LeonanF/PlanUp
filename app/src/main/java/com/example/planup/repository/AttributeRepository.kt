package com.example.planup.repository

import android.util.Log
import com.example.planup.model.Attribute
import com.example.planup.network.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttributeRepository {
    private val apiService = RetrofitInstance.apiService
    fun postAttribute(attribute: Attribute){
        apiService.postAttribute(attribute).enqueue(object: Callback <ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if(response.isSuccessful){
                    Log.d("PostAttribute", "Atributo criado com sucesso: ${response.body()}")
                } else{
                    Log.e("PostAttribute", "Falha ao criar atributo: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable){
                Log.e("PostAttribute", "Erro ao enviar o atributo: ${t.message}")
                t.printStackTrace()
            }
        })
    }
}