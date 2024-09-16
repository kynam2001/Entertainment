package com.example.ringtuneandwallpaper.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.databinding.FragmentStartBinding

class StartFragment: Fragment(){

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ringtuneButton.setOnClickListener{
            findNavController().navigate(R.id.action_startFragment_to_ringTuneFragment)
        }
        binding.wallpaperButton.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_wallpaperFragment)
        }
        binding.settingButton.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_settingFragment)
        }
    }
}