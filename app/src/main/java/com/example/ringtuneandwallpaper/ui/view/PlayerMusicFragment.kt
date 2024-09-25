package com.example.ringtuneandwallpaper.ui.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.databinding.FragmentPlayerMusicBinding
import com.example.ringtuneandwallpaper.model.RingtoneEntity
import com.example.ringtuneandwallpaper.viewmodel.MyViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerMusicFragment: Fragment() {

    private val viewModel: MyViewModel by viewModels()

    private var _binding: FragmentPlayerMusicBinding? = null
    private val binding get() = _binding!!

    private lateinit var player: ExoPlayer
    private val args: PlayerMusicFragmentArgs by navArgs()
    private var position = 0
    private var listRing = listOf<RingtoneEntity>()

    private val handler = Handler(Looper.getMainLooper())

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position = args.position
        listRing = args.listRing.toList()
        configureView()
        configureExoplayer()
        configureSeekBar()
        loadUIRefPosition()
    }

    private fun configureExoplayer(){
        player = ExoPlayer.Builder(requireContext()).build()
        val concatenatingMediaSource = ConcatenatingMediaSource()
        for(item in listRing){
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
            val ringtoneList = listRing
            ringtoneList[position].isFavorite = !ringtoneList[position].isFavorite
            listRing = ringtoneList
            setFavorite()
            lifecycleScope.launch {
                viewModel.updateRingtones(listRing[position])
            }
        }
        binding.skipNextButton.setOnClickListener {
            if(position == listRing.size - 1){
                position = 0
            }else{
                position++
            }
            player.seekTo(position, 0)
        }
        binding.skipPreviousButton.setOnClickListener {
            if(position == 0){
                position = listRing.size - 1
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

    private fun loadUIRefPosition(){
        binding.ringtoneName.text = listRing[position].name
        setFavorite()
        binding.detailButton.setOnClickListener {
            val action = PlayerMusicFragmentDirections.actionPlayerMusicFragmentToRingtoneDetailFragment(listRing.toTypedArray(), position)
            findNavController().navigate(action)
        }
    }

    private fun setFavorite(){
        if(listRing[position].isFavorite){
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_36)
        }else{
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_36)
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
                loadUIRefPosition()
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
                binding.playPauseButton.performClick()
                rotationAnimator.pause()
                handler.removeCallbacks(updateSeekBarRunnable)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                binding.playPauseButton.performClick()
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