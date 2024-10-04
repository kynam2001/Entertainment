package com.example.ringtuneandwallpaper.ui.view

import android.app.WallpaperManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.databinding.FragmentWallpaperDetailBinding
import com.example.ringtuneandwallpaper.utility.navigateBack
import com.example.ringtuneandwallpaper.utility.navigateForward
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

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
            findNavController().navigateBack(action)
        }
        binding.imageName.text = listWall[position].name
        binding.size.text = "Size: ${listWall[position].dimension}"
        binding.location.text = "Location: ${listWall[position].location}"
        binding.setWallpaperButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val inputStream = URL(listWall[position].url).openStream()
                WallpaperManager.getInstance(requireContext()).setStream(inputStream)
            }
        }
    }
}