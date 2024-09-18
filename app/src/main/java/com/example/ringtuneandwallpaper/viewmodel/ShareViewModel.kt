package com.example.ringtuneandwallpaper.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ringtuneandwallpaper.model.Repository
import com.example.ringtuneandwallpaper.model.Ringtone
import com.example.ringtuneandwallpaper.model.Wallpaper
import kotlinx.coroutines.launch

class ShareViewModel: ViewModel() {
    private val repository = Repository()
    val ringtoneList = MutableLiveData<List<Ringtone>>()
    val wallpaperList = MutableLiveData<List<Wallpaper>>()
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
                wallpaperList.value = repository.getWallpapers()
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                isLoading.value = false
            }
        }
    }

}