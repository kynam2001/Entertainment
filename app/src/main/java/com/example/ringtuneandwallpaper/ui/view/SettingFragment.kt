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
//        Log.e("Vigelos", "ifDownloadWithoutWifi: ${viewModel.ifDownloadWithoutWifi}")
//        Log.e("Vigelos", "ifDownloadedAddToFavorite: ${viewModel.ifDownloadedAddToFavorite}")
//        binding.switchWifi.isChecked = !viewModel.ifDownloadWithoutWifi
//        binding.switchFavorite.isChecked = viewModel.ifDownloadedAddToFavorite
//        binding.switchWifi.setOnCheckedChangeListener { _, isChecked ->
//            when(isChecked){
//                true -> viewModel.ifDownloadWithoutWifi = false
//                false -> viewModel.ifDownloadWithoutWifi = true
//            }
//            Log.e("Vigelos", "ifDownloadWithoutWifi: ${viewModel.ifDownloadWithoutWifi}")
//        }
//        binding.switchFavorite.setOnCheckedChangeListener { _, isChecked ->
//            when(isChecked){
//                true -> viewModel.ifDownloadedAddToFavorite = true
//                false -> viewModel.ifDownloadedAddToFavorite = false
//            }
//            Log.e("Vigelos", "ifDownloadedAddToFavorite: ${viewModel.ifDownloadedAddToFavorite}")
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}