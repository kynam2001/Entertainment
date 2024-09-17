package com.example.ringtuneandwallpaper.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.adapter.RingtoneAdapter
import com.example.ringtuneandwallpaper.data.Datasource
import com.example.ringtuneandwallpaper.databinding.FragmentRingTuneBinding

class RingTuneFragment: Fragment(){

    private var _binding: FragmentRingTuneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRingTuneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myDataset = Datasource().loadRingtone()
        binding.settingButton.setOnClickListener {
            findNavController().navigate(R.id.action_ringTuneFragment_to_settingFragment)
        }
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_ringTuneFragment_to_startFragment)
        }
        binding.searchTab.setOnClickListener {
            searchRingtone()
        }
        binding.recyclerViewRing.adapter = RingtoneAdapter(requireContext(), myDataset, findNavController())
        binding.recyclerViewRing.setHasFixedSize(true)
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

    fun searchRingtone(){
        openSearchView()
        binding.dimBackground.setOnClickListener {
            closeSearchView()
        }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.e("Vigelos", query.toString())
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