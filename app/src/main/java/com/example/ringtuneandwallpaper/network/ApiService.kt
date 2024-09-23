package com.example.ringtuneandwallpaper.network

import com.example.ringtuneandwallpaper.model.Ringtone
import com.example.ringtuneandwallpaper.model.Wallpaper
import retrofit2.http.GET

interface ApiService {
    @GET("song")
    suspend fun getRingtones(): List<Ringtone>
    @GET("image")
    suspend fun getWallpapers(): List<Wallpaper>
}
