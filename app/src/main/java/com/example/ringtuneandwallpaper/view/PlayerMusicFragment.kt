package com.example.ringtuneandwallpaper.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.RawResourceDataSource

class PlayerMusicFragment: Fragment() {

    private lateinit var exoPlayer: ExoPlayer
    private val args: PlayerMusicFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val _uri = args.uri
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        val playerView = view.findViewById<PlayerView>(R.id.playerView)
        playerView?.player = exoPlayer
        val uri = RawResourceDataSource.buildRawResourceUri(_uri)
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        super.onViewCreated(view, savedInstanceState)
    }
}