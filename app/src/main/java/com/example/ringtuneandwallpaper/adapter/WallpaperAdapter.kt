package com.example.ringtuneandwallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.model.Wallpaper
import com.example.ringtuneandwallpaper.view.WallpaperFragmentDirections
import com.example.ringtuneandwallpaper.viewmodel.ShareViewModel

class WallpaperAdapter(
    private val context: Context,
    private val dataset: List<Wallpaper>,
    private val navController: NavController
): RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {
    class WallpaperViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageView: ImageView = view.findViewById(R.id.wallpaperItem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_wallpaper, parent, false)
        return WallpaperViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        Glide.with(context).load(dataset[position].url).into(holder.imageView)
        holder.imageView.setOnClickListener {
            val action = WallpaperFragmentDirections.actionWallpaperFragmentToFullscreenImageFragment(position)
            navController.navigate(action)
        }
    }

}