package com.example.skillcinema.api

import com.example.skillcinema.data.ktorData.LoginData
import com.example.skillcinema.data.ktorData.LoginResponse
import com.example.skillcinema.data.ktorData.RegisterResponse
import com.example.skillcinema.data.ktorData.RegistrationData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/register")
    suspend fun registerUser(
        @Body registrationData: RegistrationData,
    ): RegisterResponse

    @POST("/login")
    suspend fun loginUser(
        @Body loginData: LoginData,
    ): LoginResponse
}

val ktorRetrofit = Retrofit.Builder().client(
    OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }).build()
).baseUrl("http://192.168.1.11:8080/") // Здесь укажите адрес вашего сервера Ktor
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val authApiService = ktorRetrofit.create(AuthApiService::class.java)

