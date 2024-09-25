package com.example.ringtuneandwallpaper.repository

import com.example.ringtuneandwallpaper.dao.RingtoneDataAccessObject
import com.example.ringtuneandwallpaper.dao.WallpaperDataAccessObject
import com.example.ringtuneandwallpaper.model.RingtoneApi
import com.example.ringtuneandwallpaper.model.RingtoneEntity
import com.example.ringtuneandwallpaper.model.WallpaperApi
import com.example.ringtuneandwallpaper.model.WallpaperEntity
import com.example.ringtuneandwallpaper.network.ApiService
import com.example.ringtuneandwallpaper.network.RetrofitInstance
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: ApiService,
    private val ringtoneDao: RingtoneDataAccessObject,
    private val wallpaperDao: WallpaperDataAccessObject
) {
    // Lấy danh sách ringtones, kiểm tra Room Database trước khi gọi API
    suspend fun fetchRingtones(): List<RingtoneEntity> {
        val existingRingtones = ringtoneDao.getAllRingtones().first() // Lấy từ Room
        return if (existingRingtones.isEmpty()) { // Nếu không có, gọi API và lưu vào Room
            val newRingtones = api.getRingtones()
            val ringtoneEntities = newRingtones.map { it.toEntity() }
            ringtoneDao.insertRingtone(ringtoneEntities)
            ringtoneEntities
        } else {
            existingRingtones
        }
    }

    suspend fun updateRingtone(ringtone: RingtoneEntity) {
        ringtoneDao.updateRingtone(ringtone)
    }

    // Lấy danh sách wallpapers, kiểm tra Room Database trước khi gọi API
    suspend fun fetchWallpapers(): List<WallpaperEntity> {
        val existingWallpapers = wallpaperDao.getAllWallpapers().first() // Lấy từ Room
        return if (existingWallpapers.isEmpty()) { // Nếu không có, gọi API và lưu vào Room
            val newWallpapers = api.getWallpapers()
            val wallpaperEntities = newWallpapers.map { it.toEntity() }
            wallpaperDao.insertWallpaper(wallpaperEntities)
            wallpaperEntities
        } else {
            existingWallpapers
        }
    }

    suspend fun updateWallpaper(wallpaper: WallpaperEntity) {
        wallpaperDao.updateWallpaper(wallpaper)
    }


    // Chuyển đổi RingtoneApi thành RingtoneEntity để lưu vào Room Database
    private fun RingtoneApi.toEntity(): RingtoneEntity {
        return RingtoneEntity(
            name = name,
            url = url,
            isFavorite = false
        )
    }

    // Chuyển đổi WallpaperApi thành WallpaperEntity để lưu vào Room Database
    private fun WallpaperApi.toEntity(): WallpaperEntity {
        return WallpaperEntity(
            name = name,
            url = url,
            isFavorite = false
        )
    }
}