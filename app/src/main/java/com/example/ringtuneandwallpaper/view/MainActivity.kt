package com.example.ringtuneandwallpaper.view

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ringtuneandwallpaper.databinding.ActivityMainBinding
import com.example.ringtuneandwallpaper.model.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://66ea528655ad32cda4785c45.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val songs = apiService.getSongs()
                val images = apiService.getImages()
                Log.e("Vigelos", songs[0].url)
                Log.e("Vigelos", images.toString())
            } catch (e: Exception) {
                // Handle error
            }
        }


    }
}