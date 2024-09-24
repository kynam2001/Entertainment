package com.example.ringtuneandwallpaper.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ringtuneandwallpaper.model.WallpaperEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDataAccessObject {
    @Query("SELECT * FROM wallpapers")
    fun getAllWallpapers(): Flow<List<WallpaperEntity>>

    @Query("DELETE FROM wallpapers")
    suspend fun deleteAllWallpapers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: List<WallpaperEntity>)

    @Update
    suspend fun updateWallpaper(wallpaper: WallpaperEntity)

    @Delete
    suspend fun deleteWallpaper(wallpaper: WallpaperEntity)


}