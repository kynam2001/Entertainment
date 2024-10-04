package com.example.ringtuneandwallpaper.ui.view

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.ui.adapter.WallpaperAdapter
import com.example.ringtuneandwallpaper.databinding.FragmentWallpaperBinding
import com.example.ringtuneandwallpaper.ui.adapter.OnImagesLoadCompleteListener
import com.example.ringtuneandwallpaper.utility.navigateBack
import com.example.ringtuneandwallpaper.utility.navigateForward
import com.example.ringtuneandwallpaper.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallpaperFragment: Fragment(){

    private val viewModel: MyViewModel by viewModels()

    private var _binding: FragmentWallpaperBinding? = null
    private val binding get() = _binding!!

    private val listener = object: OnImagesLoadCompleteListener {
        override fun onImagesLoadComplete() {
            viewModel.onWallpaperLoaded()
        }

    }

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
        viewModel.fetchWallpapers()
        configureRecyclerView()
        binding.backButton.setOnClickListener {
            val action = WallpaperFragmentDirections.actionWallpaperFragmentToStartFragment()
            findNavController().navigateBack(action)
        }
        binding.settingButton.setOnClickListener {
            val action = WallpaperFragmentDirections.actionWallpaperFragmentToSettingFragment()
            findNavController().navigateForward(action)
        }
        binding.imageTab.setOnClickListener {
            whenTabClicked(it)
            binding.recyclerViewWallpaper.adapter = WallpaperAdapter(
                requireContext(),
                viewModel.wallpaperList.value!!,
                findNavController(),
                listener
            )
        }
        binding.favoriteTab.setOnClickListener {
            whenTabClicked(it)
            binding.recyclerViewWallpaper.adapter = WallpaperAdapter(
                requireContext(),
                viewModel.wallpaperList.value!!.filter { wallpaper -> wallpaper.isFavorite },
                findNavController(),
                listener
            )
        }
        binding.downloadTab.setOnClickListener {
            whenTabClicked(it)
            binding.recyclerViewWallpaper.adapter = WallpaperAdapter(
                requireContext(),
                viewModel.wallpaperList.value!!.filter { wallpaper -> wallpaper.isDownloaded },
                findNavController(),
                listener
            )
        }
        binding.searchTab.setOnClickListener {
            whenTabClicked(it)
            searchWallpaper()
        }

    }

    private fun whenTabClicked(tab: View){
        val listTab = listOf(binding.imageTab, binding.favoriteTab, binding.downloadTab, binding.searchTab)
        if(listTab.contains(tab)){
            Log.e("Vigelos", tab.toString())
            tab.background = ColorDrawable(Color.parseColor("#889ADC"))
            listTab.filter { listTab -> listTab != tab }.forEach { anotherTab ->
                anotherTab.setBackgroundResource(R.drawable.button_clicked_wallpaper)
            }
        }
    }

    private fun configureRecyclerView(){
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.recyclerViewWallpaper.layoutManager = staggeredGridLayoutManager
        viewModel.isLoading.observe(viewLifecycleOwner){ loaded ->
            if(loaded){
                binding.loadingBackground.visibility = View.VISIBLE
            }
            else{
                binding.loadingBackground.apply {
                    alpha = 1f
                    animate().alpha(0f).setDuration(300).setListener(object: AnimatorListener{
                        override fun onAnimationStart(p0: Animator) {}

                        override fun onAnimationEnd(p0: Animator) {
                            visibility = View.GONE
                        }

                        override fun onAnimationCancel(p0: Animator) {
                            visibility = View.GONE
                        }

                        override fun onAnimationRepeat(p0: Animator) {}
                    })
                }
            }
        }
        viewModel.wallpaperList.observe(viewLifecycleOwner) { wallpaperList ->
            binding.recyclerViewWallpaper.adapter = WallpaperAdapter(
                requireContext(),
                wallpaperList,
                findNavController(),
                listener
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
                        viewModel.wallpaperList.value!!.filter { wallpaper -> wallpaper.name == query },
                        findNavController(),
                        listener
                    )
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