package com.example.ringtuneandwallpaper.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.ringtuneandwallpaper.dao.RingtoneDataAccessObject
import com.example.ringtuneandwallpaper.dao.WallpaperDataAccessObject
import com.example.ringtuneandwallpaper.model.RingtoneDatabase
import com.example.ringtuneandwallpaper.model.WallpaperDatabase
import com.example.ringtuneandwallpaper.network.ApiService
import com.example.ringtuneandwallpaper.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRingtoneDao(appDatabase: RingtoneDatabase): RingtoneDataAccessObject {
        return appDatabase.ringtoneDao()
    }

    @Provides
    fun provideWallpaperDao(appDatabase: WallpaperDatabase): WallpaperDataAccessObject {
        return appDatabase.wallpaperDao()
    }

    @Provides
    @Singleton
    fun provideRingtoneDatabase(@ApplicationContext context: Context): RingtoneDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            RingtoneDatabase::class.java,
            "ringtones"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideWallpaperDatabase(@ApplicationContext context: Context): WallpaperDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WallpaperDatabase::class.java,
            "wallpapers"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    }

}