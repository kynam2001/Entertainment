package com.example.ringtuneandwallpaper.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ringtuneandwallpaper.dao.WallpaperDataAccessObject

@Database(entities = [WallpaperEntity::class], version = 1, exportSchema = false)
abstract class WallpaperDatabase: RoomDatabase()  {
    abstract fun wallpaperDao(): WallpaperDataAccessObject
}