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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.databinding.FragmentPlayerMusicBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.RawResourceDataSource

class PlayerMusicFragment: Fragment() {

    private var _binding: FragmentPlayerMusicBinding? = null
    private val binding get() = _binding!!

    private lateinit var player: ExoPlayer
    private val args: PlayerMusicFragmentArgs by navArgs()
    private val handler = Handler(Looper.getMainLooper())

    private var favorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerMusicFragment_to_ringTuneFragment)
        }
        binding.detailButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerMusicFragment_to_ringtoneDetailFragment)
        }
        binding.favoriteButton.setOnClickListener {
            setFavorite()
        }
        val _uri = args.uri
        val uri = RawResourceDataSource.buildRawResourceUri(_uri)
        val mediaItem = MediaItem.fromUri(uri)
        player = ExoPlayer.Builder(requireContext()).build()
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        updateSeekBar()
        binding.playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                binding.playPauseButton.setImageResource(R.drawable.baseline_play_arrow_60)
                binding.seekBar.removeCallbacks(null)
            } else {
                player.play()
                binding.playPauseButton.setImageResource(R.drawable.baseline_pause_60)
                updateSeekBar()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        binding.seekBar.removeCallbacks(null)
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

    fun updateSeekBar(){
        val updateSeekBarRunnable = object : Runnable {
            override fun run() {
                if (player.isPlaying) {
                    // Lấy thời gian phát hiện tại và cập nhật SeekBar
                    val currentPosition = player.currentPosition.toInt()
                    binding.seekBar.progress = currentPosition

                    // Cập nhật SeekBar mỗi 1000ms (1 giây)
                    handler.postDelayed(this, 1000)
                }
            }
        }
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
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
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
            binding.seekBar.removeCallbacks(updateSeekBarRunnable)
        }
    }
}