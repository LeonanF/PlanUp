package com.example.planup.network

import com.example.planup.BuildConfig
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = BuildConfig.API_KEY

    val apiService : ApiService by lazy{
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

}