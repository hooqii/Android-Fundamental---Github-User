package com.example.githubuser.data.remote.retrofit

import com.example.githubuser.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private const val BASE_URL = BuildConfig.BASE_URL
    private const val TIMEOUT_READ = 20L // in seconds
    private const val TIMEOUT_WRITE = 20L // in seconds
    private const val TIMEOUT_CONNECT = 5L // in seconds

    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val req = chain.request()
        val requestHeaders = req.newBuilder()
            .addHeader("Authorization", BuildConfig.API_KEY)
            .build()
        chain.proceed(requestHeaders)
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val githubService: ApiService = retrofit.create(ApiService::class.java)
}