package com.example.ringtuneandwallpaper.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ringtuneandwallpaper.dao.RingtoneDataAccessObject

@Database(entities = [RingtoneEntity::class], version = 1, exportSchema = false)
abstract class RingtoneDatabase: RoomDatabase() {
    abstract fun ringtoneDao(): RingtoneDataAccessObject
}