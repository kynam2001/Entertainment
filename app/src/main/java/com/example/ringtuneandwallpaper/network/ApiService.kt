package com.example.ringtuneandwallpaper.network

import com.example.ringtuneandwallpaper.model.RingtoneApi
import com.example.ringtuneandwallpaper.model.WallpaperApi
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET("song")
    suspend fun getRingtones(): List<RingtoneApi>
    @GET("image")
    suspend fun getWallpapers(): List<WallpaperApi>
    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}
