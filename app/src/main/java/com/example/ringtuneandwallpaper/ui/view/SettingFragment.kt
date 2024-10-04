package com.example.ringtuneandwallpaper.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ringtuneandwallpaper.databinding.FragmentSettingBinding
import com.example.ringtuneandwallpaper.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment: Fragment(){

    private val viewModel: MyViewModel by viewModels()

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.quitButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.switchWifi.isChecked = viewModel.getIfDownloadOnlyWifi()
        binding.switchFavorite.isChecked = viewModel.getIfDownloadedAddToFavorite()
        binding.switchWifi.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setIfDownloadOnlyWifi(isChecked)
        }
        binding.switchFavorite.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setIfDownloadedAddToFavorite(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}