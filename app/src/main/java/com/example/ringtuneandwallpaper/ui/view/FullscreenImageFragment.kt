package com.example.ringtuneandwallpaper.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.databinding.FragmentFullscreenImageBinding
import com.example.ringtuneandwallpaper.utility.OnSwipeTouchListener
import com.example.ringtuneandwallpaper.viewmodel.ShareViewModel
@SuppressLint("ClickableViewAccessibility")
class FullscreenImageFragment: Fragment(){

    private lateinit var viewModel: ShareViewModel

    private var _binding: FragmentFullscreenImageBinding? = null
    private val binding get() = _binding!!
    private var favorite = false

    private val args: FullscreenImageFragmentArgs by navArgs()
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullscreenImageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position = args.position
        configureView()
        binding.imageName.text = viewModel.wallpaperList.value!![position].name
        Glide.with(this).load(viewModel.wallpaperList.value!![position].url).into(binding.fullscreenImageView)

    }

    private fun configureView(){
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_fullscreenImageFragment_to_wallpaperFragment)
        }
        binding.detailButton.setOnClickListener {
            val action = FullscreenImageFragmentDirections.actionFullscreenImageFragmentToWallpaperDetailFragment(position)
            findNavController().navigate(action)
        }
        binding.favoriteButton.setOnClickListener {
            setFavorite()
        }
        binding.fullscreenImageView.setOnTouchListener (object: OnSwipeTouchListener(requireContext()){
            override fun onSwipeLeft() {
                if(position == viewModel.wallpaperList.value!!.size - 1){
                    position = 0
                }
                else {
                    position += 1
                }
                binding.imageName.text = viewModel.wallpaperList.value!![position].name
                Glide.with(this@FullscreenImageFragment)
                    .load(viewModel.wallpaperList.value!![position].url).into(binding.fullscreenImageView)

            }

            override fun onSwipeRight() {
                if(position == 0){
                    position = viewModel.wallpaperList.value!!.size - 1
                }
                else{
                    position -= 1
                }
                binding.imageName.text = viewModel.wallpaperList.value!![position].name
                Glide.with(this@FullscreenImageFragment)
                    .load(viewModel.wallpaperList.value!![position].url).into(binding.fullscreenImageView)
            }
        })
    }

    private fun setFavorite(){
        if(!favorite) {
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_36)
            favorite = true
        }
        else{
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_36)
            favorite = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}