package com.example.ringtuneandwallpaper.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.model.WallpaperApi
import com.example.ringtuneandwallpaper.model.WallpaperEntity
import com.example.ringtuneandwallpaper.ui.view.WallpaperFragmentDirections
import com.example.ringtuneandwallpaper.viewmodel.ShareViewModel

class WallpaperAdapter(
    private val context: Context,
    private val viewModel: ShareViewModel?,
    private val dataset: List<WallpaperEntity>,
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
        var index = position
        // Favorite or search list
        if(viewModel?.wallpaperList?.value != null){
            index = viewModel.wallpaperList.value!!.indexOf(dataset[position])
        }
        Glide.with(context).load(dataset[position].url).into(holder.imageView)
        holder.imageView.setOnClickListener {
            val action = WallpaperFragmentDirections.actionWallpaperFragmentToFullscreenImageFragment(index)
            navController.navigate(action)
        }
    }
}