package com.example.ringtuneandwallpaper.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ringtuneandwallpaper.dao.RingtoneDataAccessObject

@Database(entities = [RingtoneEntity::class], version = 1, exportSchema = false)
abstract class RingtoneDatabase: RoomDatabase() {
    abstract fun ringtoneDao(): RingtoneDataAccessObject

    companion object{
        @Volatile private var INSTANCE: RingtoneDatabase? = null

        fun getDatabase(context: Context): RingtoneDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RingtoneDatabase::class.java,
                    "ringtones"
                    ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}