package com.example.ringtuneandwallpaper.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "wallpapers")
@Parcelize
data class WallpaperEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val url: String,
    val location: String,
    val dimension: String,
    var isFavorite: Boolean = false,
    var isDownloaded: Boolean = false
): Parcelable