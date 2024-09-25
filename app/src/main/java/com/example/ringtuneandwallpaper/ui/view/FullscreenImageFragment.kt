package com.example.ringtuneandwallpaper.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.databinding.FragmentFullscreenImageBinding
import com.example.ringtuneandwallpaper.model.WallpaperEntity
import com.example.ringtuneandwallpaper.utility.OnSwipeTouchListener
import com.example.ringtuneandwallpaper.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class FullscreenImageFragment: Fragment(){

    private val viewModel: MyViewModel by viewModels()

    private var _binding: FragmentFullscreenImageBinding? = null
    private val binding get() = _binding!!

    private val args: FullscreenImageFragmentArgs by navArgs()
    private var position = 0
    private var listWall = listOf<WallpaperEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullscreenImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position = args.position
        listWall = args.listWall.toList()
        configureView()

    }

    private fun configureView(){
        loadUIRefPosition()
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_fullscreenImageFragment_to_wallpaperFragment)
        }
        binding.detailButton.setOnClickListener {
            val action = FullscreenImageFragmentDirections.actionFullscreenImageFragmentToWallpaperDetailFragment(listWall.toTypedArray(), position)
            findNavController().navigate(action)
        }
        binding.favoriteButton.setOnClickListener {
            val wallpaperList = listWall
            wallpaperList[position].isFavorite = !wallpaperList[position].isFavorite
            listWall = wallpaperList
            setFavorite()
            lifecycleScope.launch {
                viewModel.updateWallpapers(listWall[position])
            }
        }
        binding.fullscreenImageView.setOnTouchListener (object: OnSwipeTouchListener(requireContext()){
            override fun onSwipeLeft() {
                if(position == listWall.size - 1){
                    position = 0
                }
                else {
                    position += 1
                }
                loadUIRefPosition()
            }

            override fun onSwipeRight() {
                if(position == 0){
                    position = listWall.size - 1
                }
                else{
                    position -= 1
                }
                loadUIRefPosition()
            }
        })
    }

    private fun loadUIRefPosition(){
        binding.imageName.text = listWall[position].name
        setFavorite()
        Glide.with(this).load(listWall[position].url).into(binding.fullscreenImageView)
    }

    private fun setFavorite(){
        if(listWall[position].isFavorite){
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_36)
        }else{
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_36)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}