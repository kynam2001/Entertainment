package com.example.ringtuneandwallpaper.ui.view

import android.Manifest
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.databinding.FragmentPlayerMusicBinding
import com.example.ringtuneandwallpaper.model.RingtoneEntity
import com.example.ringtuneandwallpaper.utility.navigateBack
import com.example.ringtuneandwallpaper.utility.navigateForward
import com.example.ringtuneandwallpaper.viewmodel.MyViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.InputStream

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.Q)
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

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

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

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        requestPermission()
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
            val action = PlayerMusicFragmentDirections.actionPlayerMusicFragmentToRingTuneFragment()
            findNavController().navigateForward(action)
        }
        binding.favoriteButton.setOnClickListener {
            listRing[position].isFavorite = !listRing[position].isFavorite
            setFavorite()
            lifecycleScope.launch {
                viewModel.updateRingtones(listRing[position])
            }
        }
        binding.downloadButton.setOnClickListener {
            if(listRing[position].isDownloaded){
                Toast.makeText(requireContext(), "File already downloaded", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            listRing[position].isDownloaded = true
            saveRingtone(listRing[position].url, listRing[position].name)
            if(viewModel.getIfDownloadedAddToFavorite()){
                listRing[position].isFavorite = true
                setFavorite()
            }
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
            switchDiskIcon(false)
        }
        binding.skipPreviousButton.setOnClickListener {
            if(position == 0){
                position = listRing.size - 1
            }else{
                position--
            }
            player.seekTo(position, 0)
            switchDiskIcon(true)
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

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
            }
        }
    }

    private fun loadUIRefPosition(){
        binding.ringtoneName.text = listRing[position].name.replace("_"," ")
        setFavorite()
        binding.detailButton.setOnClickListener {
            val action = PlayerMusicFragmentDirections.actionPlayerMusicFragmentToRingtoneDetailFragment(listRing.toTypedArray(), position)
            findNavController().navigateBack(action)
        }
    }

    private fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    private var downloadResultObserver: Observer<Response<ResponseBody>>? = null
    private var downloadInProgress = false

    private fun saveRingtone(url: String, fileName: String){
        if(viewModel.getIfDownloadOnlyWifi() && !isWifiConnected(requireContext())){
            Toast.makeText(requireContext(), "No wifi connection", Toast.LENGTH_SHORT).show()
            return
        }
        if(downloadInProgress) return

        downloadInProgress = true
        viewModel.downloadFile(url)

        downloadResultObserver = Observer { response ->
            if (response.isSuccessful) {
                //Lưu tệp vào bộ nhớ
                val inputStream: InputStream? = response.body()?.byteStream()
                saveFileToDownloadFolder(inputStream, fileName)
                Log.e("Vigelos", "Download success")
            } else {
                Toast.makeText(requireContext(), "Download failed", Toast.LENGTH_SHORT).show()
            }
            downloadResultObserver?.let{
                viewModel.downloadResult.removeObserver(it)
            }
            downloadResultObserver = null
            downloadInProgress = false
        }

        viewModel.downloadResult.observe(viewLifecycleOwner, downloadResultObserver!!)
    }

    private fun saveFileToDownloadFolder(inputStream: InputStream?, fileName: String){
        val resolver = requireContext().contentResolver
        // Tạo metadata cho tệp
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName) // Tên tệp
            put(MediaStore.Downloads.MIME_TYPE, "audio/mpeg")    // Loại MIME
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS) // Đường dẫn
        }
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Log.e("Vigelos", "Full Download Directory: ${downloadsDir.absolutePath}")
        Log.e("Vigelos", "Folder: ${Environment.DIRECTORY_DOWNLOADS}")
        // Chèn tệp vào MediaStore để lấy URI
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        // Nếu thành công thì tiến hành ghi dữ liệu vào tệp
        uri?.let {
            resolver.openOutputStream(it).use { outputStream ->
                inputStream?.copyTo(outputStream!!)
            }
            Toast.makeText(requireContext(), "Tệp đã lưu vào thư mục Download", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(requireContext(), "Lưu tệp thất bại", Toast.LENGTH_SHORT).show()
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
                Log.e(position.toString(), player.currentMediaItemIndex.toString())
                if(position != player.currentMediaItemIndex){
                    position = player.currentMediaItemIndex
                    switchDiskIcon(false)
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

    private fun switchDiskIcon(ifReverse: Boolean) {
        // Di chuyển imageView1 sang trái
        if(ifReverse){
            binding.diskIcon.translationX = -1000f
            binding.diskIcon.animate()
                .translationX(0f)  // Di chuyển sang phải
                .setDuration(500)
        }
        else {
            binding.diskIcon.translationX = 1000f
            binding.diskIcon.animate()
                .translationX(0f)  // Di chuyển sang trái
                .setDuration(500)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        handler.removeCallbacks(updateSeekBarRunnable)
        rotationAnimator.cancel()
        _binding = null
    }
}