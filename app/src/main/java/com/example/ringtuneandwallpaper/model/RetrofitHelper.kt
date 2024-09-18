package com.example.ringtuneandwallpaper.model

import retrofit2.http.GET

data class Song(val url: String, val name: String)
data class Image(val url: String, val name: String)

interface ApiService {
    @GET("song")
    suspend fun getSongs(): List<Song>
    @GET("image")
    suspend fun getImages(): List<Image>
}
