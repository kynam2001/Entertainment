package com.example.ringtuneandwallpaper.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ringtuneandwallpaper.dao.WallpaperDataAccessObject

@Database(entities = [WallpaperEntity::class], version = 1, exportSchema = false)
abstract class WallpaperDatabase: RoomDatabase()  {
    abstract fun wallpaperDao(): WallpaperDataAccessObject

    companion object{
        @Volatile private var INSTANCE: WallpaperDatabase? = null

        fun getDatabase(context: Context): WallpaperDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WallpaperDatabase::class.java,
                    "wallpapers"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}