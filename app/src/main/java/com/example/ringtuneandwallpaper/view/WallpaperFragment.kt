package com.example.ringtuneandwallpaper.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ringtuneandwallpaper.R
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
        binding.settingButton.setOnClickListener {
            findNavController().navigate(R.id.action_wallpaperFragment_to_settingFragment)
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_wallpaperFragment_to_startFragment)
        }
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.recyclerViewWallpaper.layoutManager = staggeredGridLayoutManager
        binding.recyclerViewWallpaper.adapter = WallpaperAdapter(requireContext(), myDataset, findNavController())
        binding.recyclerViewWallpaper.setHasFixedSize(true)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}