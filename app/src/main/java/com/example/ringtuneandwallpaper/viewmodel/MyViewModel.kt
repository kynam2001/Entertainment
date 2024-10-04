package com.example.ringtuneandwallpaper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ringtuneandwallpaper.model.RingtoneEntity
import com.example.ringtuneandwallpaper.model.WallpaperEntity
import com.example.ringtuneandwallpaper.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun downloadFile(url: String) {
        viewModelScope.launch {
            downloadResult.value = repository.downloadFile(url)
        }
    }

    fun fetchRingtones() {
        _isLoading.value = true
        viewModelScope.launch {
            val ringtones = repository.fetchRingtones()
            ringtoneList.postValue(ringtones)
            _isLoading.value = false
        }
    }

    fun updateRingtones(ringtone: RingtoneEntity){
        viewModelScope.launch {
            repository.updateRingtone(ringtone)
        }
    }

    fun fetchWallpapers() {
        _isLoading.value = true
        viewModelScope.launch {
            val wallpapers = repository.fetchWallpapers()
            wallpaperList.postValue(wallpapers)
        }
    }

    fun onWallpaperLoaded(){
        _isLoading.value = false
    }

    fun updateWallpapers(wallpaper: WallpaperEntity){
        viewModelScope.launch {
            repository.updateWallpaper(wallpaper)
        }
    }

    fun setIfDownloadedAddToFavorite(value: Boolean){
        repository.setIfDownloadedAddToFavorite(value)
    }

    fun getIfDownloadedAddToFavorite(): Boolean{
        return repository.getIfDownloadedAddToFavorite()
    }

    fun setIfDownloadOnlyWifi(value: Boolean){
        repository.setDownloadOnlyWifi(value)
    }

    fun getIfDownloadOnlyWifi(): Boolean{
        return repository.getDownloadOnlyWifi()
    }

}