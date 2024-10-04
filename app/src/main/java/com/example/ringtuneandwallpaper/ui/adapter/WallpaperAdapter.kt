package com.example.ringtuneandwallpaper.ui.adapter

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.model.WallpaperEntity
import com.example.ringtuneandwallpaper.ui.view.WallpaperFragmentDirections
import com.example.ringtuneandwallpaper.utility.navigateForward

class WallpaperAdapter(
    private val context: Context,
    private val dataset: List<WallpaperEntity>,
    private val navController: NavController,
    private val listener: OnImagesLoadCompleteListener
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
        Glide.with(context)
            .load(dataset[position].url)
            .listener(object: RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if(position == dataset.size - 1) {
                        listener.onImagesLoadComplete()
                    }
                    return false
                }

            })
            .into(holder.imageView)
        holder.imageView.setOnClickListener {
            holder.imageView.animate().apply {
                scaleX(1.2f)
                scaleY(1.2f)
                duration = 300
                setListener(object: AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {}

                    override fun onAnimationEnd(p0: Animator) {
                        val action = WallpaperFragmentDirections.actionWallpaperFragmentToFullscreenImageFragment(dataset.toTypedArray(), position)
                        navController.navigateForward(action)
                    }

                    override fun onAnimationCancel(p0: Animator) {
                        val action = WallpaperFragmentDirections.actionWallpaperFragmentToFullscreenImageFragment(dataset.toTypedArray(), position)
                        navController.navigateForward(action)
                    }

                    override fun onAnimationRepeat(p0: Animator) {}

                })
            }
        }
    }
}

interface OnImagesLoadCompleteListener {
    fun onImagesLoadComplete()
}