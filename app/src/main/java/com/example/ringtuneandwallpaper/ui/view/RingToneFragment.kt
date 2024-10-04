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
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.ui.adapter.RingtoneAdapter
import com.example.ringtuneandwallpaper.databinding.FragmentRingToneBinding
import com.example.ringtuneandwallpaper.utility.navigateBack
import com.example.ringtuneandwallpaper.utility.navigateForward
import com.example.ringtuneandwallpaper.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RingToneFragment: Fragment(){

    private val viewModel: MyViewModel by viewModels()
    private var _binding: FragmentRingToneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRingToneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchRingtones()
        configureRecyclerView()
        binding.backButton.setOnClickListener {
            val action = RingToneFragmentDirections.actionRingTuneFragmentToStartFragment()
            findNavController().navigateForward(action)
        }
        binding.settingButton.setOnClickListener {
            val action = RingToneFragmentDirections.actionRingTuneFragmentToSettingFragment()
            findNavController().navigateBack(action)
        }
        binding.ringtoneTab.setOnClickListener {
            whenTabClicked(it)
            binding.recyclerViewRingtone.adapter = RingtoneAdapter(
                viewModel.ringtoneList.value!!,
                findNavController()
            )
        }
        binding.favoriteTab.setOnClickListener {
            whenTabClicked(it)
            binding.recyclerViewRingtone.adapter = RingtoneAdapter(
                viewModel.ringtoneList.value!!.filter { ringtone -> ringtone.isFavorite },
                findNavController()
            )
        }
        binding.downloadTab.setOnClickListener {
            whenTabClicked(it)
            binding.recyclerViewRingtone.adapter = RingtoneAdapter(
                viewModel.ringtoneList.value!!.filter { ringtone -> ringtone.isDownloaded },
                findNavController()
            )
        }
        binding.searchTab.setOnClickListener {
            whenTabClicked(it)
            searchRingtone()
        }
    }

    private fun whenTabClicked(tab: View){
        val listTab = listOf(binding.ringtoneTab, binding.favoriteTab, binding.downloadTab, binding.searchTab)
        if(listTab.contains(tab)){
            Log.e("Vigelos", tab.toString())
            tab.background = ColorDrawable(Color.parseColor("#DCC5C5"))
            listTab.filter { listTab -> listTab != tab }.forEach { anotherTab ->
                anotherTab.setBackgroundResource(R.drawable.button_clicked_ringtone)
            }
        }
    }

    private fun configureRecyclerView(){
        viewModel.isLoading.observe(viewLifecycleOwner){ loaded ->
            if(loaded){
                binding.loadingBackground.visibility = View.VISIBLE
            }
            else{
                binding.loadingBackground.apply {
                    alpha = 1f
                    animate().alpha(0f).setDuration(300).setListener(object: AnimatorListener {
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
        viewModel.ringtoneList.observe(viewLifecycleOwner) { ringtoneList ->
            binding.recyclerViewRingtone.adapter = RingtoneAdapter(
                ringtoneList,
                findNavController()
            )
        }
        binding.recyclerViewRingtone.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        closeSearchView()
        _binding = null
    }

    private fun searchRingtone(){
        openSearchView()
        binding.dimBackground.setOnClickListener {
            closeSearchView()
        }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(viewModel.ringtoneList.value!!.any { ringtone -> ringtone.name == query }) {
                    binding.recyclerViewRingtone.adapter = RingtoneAdapter(
                        viewModel.ringtoneList.value!!.filter { ringtone -> ringtone.name == query },
                        findNavController()
                    )
                }
                else{
                    Toast.makeText(requireContext(),"No ringtone found", Toast.LENGTH_SHORT).show()
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

    private fun closeSearchView() {
        binding.searchView.isIconified = true
        binding.searchView.clearFocus()
        binding.searchView.visibility = View.GONE
        binding.dimBackground.visibility = View.GONE
    }
}