package com.example.ringtuneandwallpaper.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlin.time.Duration

@Entity(tableName = "ringtones")
@Parcelize
data class RingtoneEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val url: String,
    val duration: Int,
    var isFavorite: Boolean = false,
    var isDownloaded: Boolean = false
): Parcelable
