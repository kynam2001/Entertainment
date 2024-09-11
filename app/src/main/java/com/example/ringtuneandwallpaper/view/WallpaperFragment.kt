package com.example.ringtuneandwallpaper.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ringtuneandwallpaper.adapter.WallpaperAdapter
import com.example.ringtuneandwallpaper.data.Datasource
import com.example.ringtuneandwallpaper.databinding.FragmentWallpaperBinding

class WallpaperFragment: Fragment(){

    private var _binding: FragmentWallpaperBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWallpaperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myDataset = Datasource().loadWallpaper()
        binding.recyclerViewWall.adapter = WallpaperAdapter(requireContext(), myDataset)
        binding.recyclerViewWall.setHasFixedSize(true)

    }
}