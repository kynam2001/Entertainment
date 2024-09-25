package com.example.ringtuneandwallpaper.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.ui.adapter.WallpaperAdapter
import com.example.ringtuneandwallpaper.databinding.FragmentWallpaperBinding
import com.example.ringtuneandwallpaper.viewmodel.ShareViewModel

class WallpaperFragment: Fragment(){

    private lateinit var viewModel: ShareViewModel

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
        viewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]
        viewModel.fetchWallpapers()
        binding.settingButton.setOnClickListener {
            findNavController().navigate(R.id.action_wallpaperFragment_to_settingFragment)
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_wallpaperFragment_to_startFragment)
        }
        binding.imageTab.setOnClickListener {
            binding.recyclerViewWallpaper.adapter = WallpaperAdapter(
                requireContext(),
                null,
                viewModel.wallpaperList.value!!,
                findNavController()
            )
        }
        binding.favoriteTab.setOnClickListener {
            binding.recyclerViewWallpaper.adapter = WallpaperAdapter(
                requireContext(),
                viewModel,
                viewModel.wallpaperList.value!!.filter { wallpaper -> wallpaper.isFavorite },
                findNavController()
            )
        }
        binding.searchTab.setOnClickListener {
            searchWallpaper()
        }
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.recyclerViewWallpaper.layoutManager = staggeredGridLayoutManager
        viewModel.wallpaperList.observe(viewLifecycleOwner) { wallpaperList ->
            binding.recyclerViewWallpaper.adapter = WallpaperAdapter(
                requireContext(),
                null,
                wallpaperList,
                findNavController()
            )
        }
        binding.recyclerViewWallpaper.setHasFixedSize(true)

    }

    override fun onPause() {
        super.onPause()
        closeSearchView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        closeSearchView()
        _binding = null
    }

    private fun searchWallpaper() {
        openSearchView()
        binding.dimBackground.setOnClickListener {
            closeSearchView()
        }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(viewModel.wallpaperList.value!!.any { wallpaper -> wallpaper.name == query }){
                    binding.recyclerViewWallpaper.adapter = WallpaperAdapter(
                        requireContext(),
                        viewModel,
                        viewModel.wallpaperList.value!!.filter { wallpaper -> wallpaper.name == query },
                        findNavController())
                }
                else{
                    Toast.makeText(requireContext(),"No wallpaper found", Toast.LENGTH_SHORT).show()
                }
                closeSearchView()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun openSearchView() {
        binding.searchView.visibility = View.VISIBLE
        binding.searchView.isIconified = false
        binding.searchView.requestFocus()
        binding.dimBackground.visibility = View.VISIBLE
    }

    fun closeSearchView() {
        binding.searchView.isIconified = true
        binding.searchView.clearFocus()
        binding.searchView.visibility = View.GONE
        binding.dimBackground.visibility = View.GONE
    }
}