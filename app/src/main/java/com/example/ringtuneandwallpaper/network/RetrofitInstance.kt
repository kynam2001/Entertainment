package com.example.ringtuneandwallpaper.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://66ea528655ad32cda4785c45.mockapi.io/") // URL gốc của API
            .addConverterFactory(GsonConverterFactory.create()) // Chuyển đổi JSON thành đối tượng Kotlin
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java) // Khởi tạo service từ Retrofit
    }
}
