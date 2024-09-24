package com.example.ringtuneandwallpaper.network

import com.example.ringtuneandwallpaper.model.RingtoneApi
import com.example.ringtuneandwallpaper.model.WallpaperApi
import retrofit2.http.GET

interface ApiService {
    @GET("song")
    suspend fun getRingtones(): List<RingtoneApi>
    @GET("image")
    suspend fun getWallpapers(): List<WallpaperApi>
}
