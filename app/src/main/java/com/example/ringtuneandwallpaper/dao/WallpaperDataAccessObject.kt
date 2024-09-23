package com.example.ringtuneandwallpaper.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ringtuneandwallpaper.model.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDataAccessObject {
    @Query("SELECT * FROM wallpapers")
    fun getAllWallpapers(): Flow<List<Wallpaper>>

    @Query("DELETE FROM wallpapers")
    suspend fun deleteAllWallpapers()

    @Insert
    suspend fun insertWallpaper(wallpaper: Wallpaper)

    @Update
    suspend fun updateWallpaper(wallpaper: Wallpaper)

    @Delete
    suspend fun deleteWallpaper(wallpaper: Wallpaper)


}