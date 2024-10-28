package com.example.eventdicoding.data.retrofit

import com.loopj.android.http.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
   private val loggingInterceptor = HttpLoggingInterceptor().apply {
       level = HttpLoggingInterceptor.Level.BODY
   }

       private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://event-api.dicoding.dev/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun create(): ApiService {
        return retrofit.create(ApiService::class.java)
        }
    }


