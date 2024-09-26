package com.example.ringtuneandwallpaper.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    val downloadResult = MutableLiveData<Response<ResponseBody>>()
    val ringtoneList = MutableLiveData<List<RingtoneEntity>>()
    var wallpaperList = MutableLiveData<List<WallpaperEntity>>()

    fun downloadFile(url: String) {
        viewModelScope.launch {
            downloadResult.value = repository.downloadFile(url)
        }
    }

    fun fetchRingtones() {
        viewModelScope.launch {
            val ringtones = repository.fetchRingtones()
            ringtoneList.postValue(ringtones)
        }
    }

    fun updateRingtones(ringtone: RingtoneEntity){
        viewModelScope.launch {
            repository.updateRingtone(ringtone)
        }
    }

    fun fetchWallpapers() {
        viewModelScope.launch {
            val wallpapers = repository.fetchWallpapers()
            wallpaperList.postValue(wallpapers)
        }
    }

    fun updateWallpapers(wallpaper: WallpaperEntity){
        viewModelScope.launch {
            repository.updateWallpaper(wallpaper)
        }

    }

}