package com.example.ringtuneandwallpaper.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.doOnDetach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.databinding.FragmentPlayerMusicBinding
import com.example.ringtuneandwallpaper.viewmodel.ShareViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class PlayerMusicFragment: Fragment() {

    private lateinit var viewModel: ShareViewModel

    private var _binding: FragmentPlayerMusicBinding? = null
    private val binding get() = _binding!!

    private lateinit var player: ExoPlayer
    private val args: PlayerMusicFragmentArgs by navArgs()
    private val handler = Handler(Looper.getMainLooper())

    private var favorite = false

    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            if(player.isPlaying){
                // Lấy thời gian phát hiện tại và cập nhật SeekBar
                val currentPosition = player.currentPosition.toInt()
                binding.seekBar.progress = currentPosition

                // Cập nhật SeekBar mỗi 100ms (0.1 giây)
                handler.postDelayed(this, 100)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerMusicBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ShareViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = args.position
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerMusicFragment_to_ringTuneFragment)
        }
        binding.detailButton.setOnClickListener {
            val action = PlayerMusicFragmentDirections.actionPlayerMusicFragmentToRingtoneDetailFragment(position)
            findNavController().navigate(action)
        }
        binding.favoriteButton.setOnClickListener {
            setFavorite()
        }
        binding.ringtoneName.text = viewModel.ringtoneList.value!![position].name
        val mediaItem = MediaItem.fromUri(viewModel.ringtoneList.value!![position].url)
        player = ExoPlayer.Builder(requireContext()).build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        configureSeekBar()
        binding.playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                binding.playPauseButton.setImageResource(R.drawable.baseline_play_arrow_60)
                handler.removeCallbacks(updateSeekBarRunnable)
            } else {
                player.play()
                binding.playPauseButton.setImageResource(R.drawable.baseline_pause_60)
                handler.post(updateSeekBarRunnable)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        handler.removeCallbacks(updateSeekBarRunnable)
        _binding = null
    }

    private fun setFavorite(){
        if(!favorite) {
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_36)
            favorite = true
        }
        else{
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_36)
            favorite = false
        }
    }

    private fun configureSeekBar(){
        player.addListener(object: Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                when(playbackState){
                    Player.STATE_READY -> {
                        binding.seekBar.max = player.duration.toInt()
                        handler.post(updateSeekBarRunnable)
                    }
                    Player.STATE_ENDED -> {
                        binding.playPauseButton.setImageResource(R.drawable.baseline_play_arrow_60)
                        binding.seekBar.progress = 0
                        handler.removeCallbacks(updateSeekBarRunnable)
                    }
                }
            }
        })
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                handler.removeCallbacks(updateSeekBarRunnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                handler.post(updateSeekBarRunnable)
            }

        })
        binding.seekBar.doOnDetach {
            handler.removeCallbacks(updateSeekBarRunnable)
        }
    }
}