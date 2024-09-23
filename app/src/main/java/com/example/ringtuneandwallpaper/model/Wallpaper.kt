package com.example.ringtuneandwallpaper.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers")
data class Wallpaper(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, val name: String, val url: String)
