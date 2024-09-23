package com.example.ringtuneandwallpaper.repository

import com.example.ringtuneandwallpaper.model.Ringtone
import com.example.ringtuneandwallpaper.model.Wallpaper
import com.example.ringtuneandwallpaper.network.RetrofitInstance

class Repository {
    private val api = RetrofitInstance.api
    suspend fun getRingtones(): List<Ringtone> {
        return api.getRingtones()
    }
    suspend fun getWallpapers(): List<Wallpaper> {
        return api.getWallpapers()
    }
}