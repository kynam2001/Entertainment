package com.example.ringtuneandwallpaper.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.databinding.FragmentRingtoneDetailBinding

class RingtoneDetailFragment: Fragment(){

    private var _binding: FragmentRingtoneDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RingtoneDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRingtoneDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = args.position
        val listRing = args.listRing.toList()
        binding.backButton.setOnClickListener {
            val action = RingtoneDetailFragmentDirections.actionRingtoneDetailFragmentToPlayerMusicFragment(listRing.toTypedArray(), position)
            findNavController().navigate(action)
        }
        binding.ringtoneName.text = listRing[position].name
    }
}