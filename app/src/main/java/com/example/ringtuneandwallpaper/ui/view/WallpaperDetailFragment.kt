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
import com.example.ringtuneandwallpaper.viewmodel.MyViewModel

class WallpaperDetailFragment: Fragment(){

    private lateinit var viewModel: MyViewModel

    private var _binding: FragmentWallpaperDetailBinding? = null
    private val binding get() = _binding!!

    private val args: WallpaperDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
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
        binding.imageName.text = viewModel.wallpaperList.value!![position].name
    }
}