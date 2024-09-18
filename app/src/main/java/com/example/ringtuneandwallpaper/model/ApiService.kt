package com.example.ringtuneandwallpaper.model

import retrofit2.http.GET

interface ApiService {
    @GET("song")
    suspend fun getRingtones(): List<Ringtone>
    @GET("image")
    suspend fun getWallpapers(): List<Wallpaper>
}
