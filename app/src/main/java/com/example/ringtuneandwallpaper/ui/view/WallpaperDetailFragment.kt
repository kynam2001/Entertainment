package com.example.ringtuneandwallpaper.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.databinding.FragmentWallpaperDetailBinding

class WallpaperDetailFragment: Fragment(){

    private var _binding: FragmentWallpaperDetailBinding? = null
    private val binding get() = _binding!!

    private val args: WallpaperDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = args.position
        val listWall = args.listWall.toList()
        binding.backButton.setOnClickListener {
            val action = WallpaperDetailFragmentDirections.actionWallpaperDetailFragmentToFullscreenImageFragment(listWall.toTypedArray(), position)
            findNavController().navigate(action)
        }
        binding.imageName.text = listWall[position].name
    }
}