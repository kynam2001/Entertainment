package com.example.ringtuneandwallpaper.repository

import com.example.ringtuneandwallpaper.model.RingtoneApi
import com.example.ringtuneandwallpaper.model.WallpaperApi
import com.example.ringtuneandwallpaper.network.RetrofitInstance

class Repository {
    private val api = RetrofitInstance.api
    suspend fun getRingtones(): List<RingtoneApi> {
        return api.getRingtones()
    }
    suspend fun getWallpapers(): List<WallpaperApi> {
        return api.getWallpapers()
    }
}