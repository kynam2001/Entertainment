package com.example.ringtuneandwallpaper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ringtuneandwallpaper.dao.RingtoneDataAccessObject
import com.example.ringtuneandwallpaper.dao.WallpaperDataAccessObject
import com.example.ringtuneandwallpaper.model.RingtoneApi
import com.example.ringtuneandwallpaper.model.RingtoneDatabase
import com.example.ringtuneandwallpaper.model.RingtoneEntity
import com.example.ringtuneandwallpaper.model.WallpaperApi
import com.example.ringtuneandwallpaper.model.WallpaperDatabase
import com.example.ringtuneandwallpaper.model.WallpaperEntity
import com.example.ringtuneandwallpaper.repository.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MyViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository()
    val ringtoneDataAccessObject: RingtoneDataAccessObject = RingtoneDatabase.getDatabase(application.applicationContext).ringtoneDao()
    val wallpaperDataAccessObject: WallpaperDataAccessObject = WallpaperDatabase.getDatabase(application.applicationContext).wallpaperDao()
    val ringtoneList = MutableLiveData<List<RingtoneEntity>>()
    var wallpaperList = MutableLiveData<List<WallpaperEntity>>()
    private val isLoading = MutableLiveData<Boolean>()

    private fun RingtoneApi.toEntity(): RingtoneEntity {
        return RingtoneEntity(
            name = name,
            url = url,
            isFavorite = false
        )
    }

    fun fetchRingtones() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val existingRingtones = ringtoneDataAccessObject.getAllRingtones().first() // Lấy dữ liệu hiện có
                if (existingRingtones.isEmpty()) { // Nếu không có dữ liệu, gọi API
                    val newRingtones = repository.getRingtones()
                    ringtoneDataAccessObject.insertRingtone(newRingtones.map { it.toEntity() })
                }
                ringtoneDataAccessObject.getAllRingtones().collect{ list ->
                    ringtoneList.postValue(list)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                isLoading.value = false
            }
        }
    }

    private fun WallpaperApi.toEntity(): WallpaperEntity {
        return WallpaperEntity(
            name = name,
            url = url,
            isFavorite = false
        )
    }

    fun fetchWallpapers(){
        viewModelScope.launch {
            isLoading.value = true
            try {
                val existingWallpapers = wallpaperDataAccessObject.getAllWallpapers().first()
                if(existingWallpapers.isEmpty()) {
                    val newWallpapers = repository.getWallpapers()
                    wallpaperDataAccessObject.insertWallpaper(newWallpapers.map { it.toEntity() })
                }
                wallpaperDataAccessObject.getAllWallpapers().collect{ list ->
                    wallpaperList.postValue(list)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                isLoading.value = false
            }
        }
    }


}