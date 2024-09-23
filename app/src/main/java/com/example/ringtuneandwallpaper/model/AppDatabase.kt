package com.example.ringtuneandwallpaper.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ringtuneandwallpaper.dao.WallpaperDataAccessObject

@Database(entities = [Wallpaper::class], version = 1)
abstract class AppDatabase: RoomDatabase()  {
    abstract fun wallpaperDao(): WallpaperDataAccessObject

    companion object{
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wallpapers"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}