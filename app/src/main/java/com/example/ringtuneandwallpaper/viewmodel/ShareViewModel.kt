package com.example.ringtuneandwallpaper.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ringtuneandwallpaper.model.AppDatabase
import com.example.ringtuneandwallpaper.repository.Repository
import com.example.ringtuneandwallpaper.model.Ringtone
import com.example.ringtuneandwallpaper.model.Wallpaper
import com.example.ringtuneandwallpaper.dao.WallpaperDataAccessObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShareViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository()
    private val wallpaperDataAccessObject: WallpaperDataAccessObject = AppDatabase.getDatabase(application.applicationContext).wallpaperDao()
    val ringtoneList = MutableLiveData<List<Ringtone>>()
    var wallpaperList = MutableLiveData<List<Wallpaper>>()
    private val isLoading = MutableLiveData<Boolean>()
    fun fetchRingtones() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                ringtoneList.value = repository.getRingtones()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                isLoading.value = false
            }
        }
    }
    fun fetchWallpapers(){
        viewModelScope.launch {
            isLoading.value = true
            try {
                wallpaperDataAccessObject.deleteAllWallpapers()
                val newWallpapers = repository.getWallpapers()
                newWallpapers.forEach { wallpaper ->
                    wallpaperDataAccessObject.insertWallpaper(wallpaper)
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