package com.example.ringtuneandwallpaper.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
        binding.recyclerViewRing.adapter = RingtoneAdapter(requireContext(), myDataset, findNavController())
        binding.recyclerViewRing.setHasFixedSize(true)
    }
}