package com.example.ringtuneandwallpaper.model

class Repository {
    private val api = RetrofitInstance.api
    suspend fun getRingtones(): List<Ringtone> {
        return api.getRingtones()
    }
    suspend fun getWallpapers(): List<Wallpaper> {
        return api.getWallpapers()
    }
}