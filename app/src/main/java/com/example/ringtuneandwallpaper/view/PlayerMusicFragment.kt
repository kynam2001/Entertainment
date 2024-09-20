package com.example.ringtuneandwallpaper.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class PlayerMusicFragment: Fragment() {

    private lateinit var viewModel: ShareViewModel

    private var _binding: FragmentPlayerMusicBinding? = null
    private val binding get() = _binding!!

    private lateinit var player: ExoPlayer
    private val args: PlayerMusicFragmentArgs by navArgs()
    private var position = 0

    private val handler = Handler(Looper.getMainLooper())

    private var favorite = false

    private lateinit var rotationAnimator: ObjectAnimator
    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            if(player.isPlaying){
                val currentPosition = player.currentPosition.toInt()
                binding.seekBar.progress = currentPosition
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
        viewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position = args.position
        configureView()
        configureExoplayer()
        configureSeekBar()
        loadUIRefPosition(position)
    }

    private fun configureExoplayer(){
        player = ExoPlayer.Builder(requireContext()).build()
        val concatenatingMediaSource = ConcatenatingMediaSource()
        for(item in viewModel.ringtoneList.value!!){
            val mediaItem = MediaItem.fromUri(item.url)
            val mediaSource = ProgressiveMediaSource
                .Factory(DefaultDataSource.Factory(requireContext())).createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        player.setMediaSource(concatenatingMediaSource)
        player.seekTo(position, 0)
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.prepare()
        player.play()
    }

    private fun configureView(){
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerMusicFragment_to_ringTuneFragment)
        }
        binding.favoriteButton.setOnClickListener {
            setFavorite()
        }
        binding.skipNextButton.setOnClickListener {
            if(position == viewModel.ringtoneList.value!!.size - 1){
                position = 0
            }else{
                position++
            }
            player.seekTo(position, 0)
        }
        binding.skipPreviousButton.setOnClickListener {
            if(position == 0){
                position = viewModel.ringtoneList.value!!.size - 1
            }else{
                position--
            }
            player.seekTo(position, 0)
        }
        binding.playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                rotationAnimator.pause()
                binding.playPauseButton.setImageResource(R.drawable.baseline_play_arrow_60)
                handler.removeCallbacks(updateSeekBarRunnable)
            } else {
                player.play()
                rotationAnimator.resume()
                binding.playPauseButton.setImageResource(R.drawable.baseline_pause_60)
                handler.post(updateSeekBarRunnable)
            }
        }
        rotationAnimator = ObjectAnimator.ofFloat(binding.diskIcon, "rotation", 0f, 360f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            interpolator = null
        }
        rotationAnimator.start()
    }

    private fun loadUIRefPosition(position: Int){
        binding.ringtoneName.text = viewModel.ringtoneList.value!![position].name
        binding.detailButton.setOnClickListener {
            val action = PlayerMusicFragmentDirections.actionPlayerMusicFragmentToRingtoneDetailFragment(position)
            findNavController().navigate(action)
        }
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

                    Player.STATE_BUFFERING -> {

                    }

                    Player.STATE_IDLE -> {

                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                if(position != player.currentMediaItemIndex){
                    position = player.currentMediaItemIndex
                }
                loadUIRefPosition(position)
                rotationAnimator.cancel()
                rotationAnimator.start()
            }
        })
        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                rotationAnimator.pause()
                handler.removeCallbacks(updateSeekBarRunnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                rotationAnimator.resume()
                handler.post(updateSeekBarRunnable)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        handler.removeCallbacks(updateSeekBarRunnable)
        rotationAnimator.cancel()
        _binding = null
    }
}