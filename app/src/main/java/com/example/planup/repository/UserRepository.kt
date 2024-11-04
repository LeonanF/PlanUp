package com.example.planup.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.planup.model.ImageSaveResult
import com.example.planup.model.User
import com.example.planup.network.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UserRepository {
    private val apiService = RetrofitInstance.apiService

    fun postUser(user: User, callback: (Boolean) -> Unit) {
        apiService.postUser(user).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(p0: Call<ResponseBody>, p1: Response<ResponseBody>) {
                if (p1.isSuccessful) {
                    callback(true)
                    Log.d("UserRepository", "Usuário cadastrado com sucesso")
                } else {
                    callback(false)
                    Log.e("UserRepository", "Erro ao cadastrar usuário: ${p1.code()}")
                }
            }

            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                callback(false)
                Log.e("UserRepository", "Falha ao cadastrar usuário: ${p1.message}")
            }
        })
    }

    fun fetchUserByEmail(email: String, callback: (User?) -> Unit) {
        apiService.fetchUserByEmail(email).enqueue(object : Callback<User>{
            override fun onResponse(p0: Call<User>, p1: Response<User>) {
                if (p1.isSuccessful) {
                    callback(p1.body())
                    Log.d("UserRepository", "Usuario carregado com sucesso")
                } else {
                    callback(null)
                    Log.e("UserRepository", "Erro ao carregar usuario: ${p1.code()}")
                }
            }

            override fun onFailure(p0: Call<User>, p1: Throwable) {
                callback(null)
                Log.e("UserRepository", "Falha ao carregar usuario: ${p1.message}")
            }
        })
    }

    fun fetchUserById(userId: String, callback: (User?) -> Unit) {
        apiService.fetchUserById(userId).enqueue(object : Callback<User>{
            override fun onResponse(p0: Call<User>, p1: Response<User>) {
                if (p1.isSuccessful) {
                    callback(p1.body())
                    Log.d("UserRepository", "Usuario carregado com sucesso")
                } else {
                    callback(null)
                    Log.e("UserRepository", "Erro ao carregar usuario: ${p1.code()}")
                }
            }

            override fun onFailure(p0: Call<User>, p1: Throwable) {
                callback(null)
                Log.e("UserRepository", "Falha ao carregar usuario: ${p1.message}")
            }
        })
    }

    fun deleteUser(userId: String) {
        apiService.deleteUSer(userId).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(p0: Call<ResponseBody>, p1: Response<ResponseBody>) {
                if (p1.isSuccessful) {
                    Log.d("UserRepository", "Usuario deletado com sucesso")
                } else {
                    Log.e("UserRepository", "Erro ao deletar usuario: ${p1.code()}")
                }
            }

            override fun onFailure(p0: Call<ResponseBody>, p1: Throwable) {
                Log.e("UserRepository", "Falha ao deletar usuario: ${p1.message}")
            }
        })
    }

    // Função para salvar a imagem no armazenamento interno com detecção de erro
    fun saveImageToInternalStorage(context: Context, uri: Uri): ImageSaveResult {
        return try {
            val imageFile = File(context.filesDir, "profile_image.jpg")
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(imageFile)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            ImageSaveResult(uri = Uri.fromFile(imageFile), success = true)
        } catch (e: Exception) {
            e.printStackTrace()
            ImageSaveResult(success = false, errorMessage = "Erro ao salvar a imagem: ${e.message}")
        }
    }

    // Função para carregar a imagem salva do armazenamento interno
    fun loadImageFromInternalStorage(context: Context): Uri? {
        val imageFile = File(context.filesDir, "profile_image.jpg")
        return if (imageFile.exists()) Uri.fromFile(imageFile) else null
    }
}