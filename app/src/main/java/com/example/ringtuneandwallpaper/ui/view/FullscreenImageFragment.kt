package com.example.ringtuneandwallpaper.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.bumptech.glide.Glide
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.databinding.FragmentFullscreenImageBinding
import com.example.ringtuneandwallpaper.model.WallpaperEntity
import com.example.ringtuneandwallpaper.utility.OnSwipeTouchListener
import com.example.ringtuneandwallpaper.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
@RequiresApi(Build.VERSION_CODES.Q)
class FullscreenImageFragment: Fragment(){

    private val viewModel: MyViewModel by viewModels()

    private var _binding: FragmentFullscreenImageBinding? = null
    private val binding get() = _binding!!

    private val args: FullscreenImageFragmentArgs by navArgs()
    private var position = 0
    private var listWall = listOf<WallpaperEntity>()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullscreenImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position = args.position
        listWall = args.listWall.toList()
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        requestPermission()
        configureView()
    }

    private fun configureView(){
        loadUIRefPosition()
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_fullscreenImageFragment_to_wallpaperFragment)
        }
        binding.detailButton.setOnClickListener {
            val action = FullscreenImageFragmentDirections.actionFullscreenImageFragmentToWallpaperDetailFragment(listWall.toTypedArray(), position)
            findNavController().navigate(action)
        }
        binding.downloadButton.setOnClickListener {
            if(listWall[position].isDownloaded){
                Toast.makeText(requireContext(), "File already downloaded", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            listWall[position].isDownloaded = true
            saveWallpaper(listWall[position].url, listWall[position].name)
            lifecycleScope.launch {
                Log.e("Vigelos", "updated")
                viewModel.updateWallpapers(listWall[position])
            }
        }
        binding.favoriteButton.setOnClickListener {
            listWall[position].isFavorite = !listWall[position].isFavorite
            setFavorite()
            lifecycleScope.launch {
                viewModel.updateWallpapers(listWall[position])
            }
        }
        binding.fullscreenImageView.setOnTouchListener (object: OnSwipeTouchListener(requireContext()){
            override fun onSwipeLeft() {
                if(position == listWall.size - 1){
                    position = 0
                }
                else {
                    position += 1
                }
                loadUIRefPosition()
            }

            override fun onSwipeRight() {
                if(position == 0){
                    position = listWall.size - 1
                }
                else{
                    position -= 1
                }
                loadUIRefPosition()
            }
        })
    }

    private var downloadResultObserver: Observer<Response<ResponseBody>>? = null
    private var downloadInProgress = false

    private fun saveWallpaper(url: String, fileName: String){
        if(downloadInProgress) return

        downloadInProgress = true
        viewModel.downloadFile(url)

        downloadResultObserver = Observer { response ->
            if (response.isSuccessful) {
                //Lưu tệp vào bộ nhớ
                val inputStream: InputStream? = response.body()?.byteStream()
                saveFileToDownloadFolder(inputStream, fileName)
//                updateDatabase()
                Log.e("Vigelos", "download success")
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
            put(MediaStore.Downloads.MIME_TYPE, "image/jpeg")    // Loại MIME
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS) // Đường dẫn
        }
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

//    private fun saveFile(inputStream: InputStream?, file: File){
//        file.outputStream().use { outputStream ->
//            inputStream.use { inputStream ->
//                inputStream?.copyTo(outputStream)
//            }
//        }
//    }

//    private fun getUniqueFileName(directory: File, fileName: String): File {
//        var file = File(directory, fileName)
//
//        // Tách tên file và phần mở rộng (extension)
//        val fileNameWithoutExtension = file.nameWithoutExtension
//        val extension = file.extension
//
//        var counter = 1
//        // Kiểm tra nếu file đã tồn tại, tiếp tục thêm số vào tên file
//        while (file.exists()) {
//            // Tạo tên file mới với số đếm (counter)
//            val newFileName = "$fileNameWithoutExtension($counter).$extension"
//            file = File(directory, newFileName)
//            counter++
//        }
//        return file
//    }
//
//    private fun promptOverwrite(inputStream: InputStream?, directory: File, fileName: String){
//        // Hiển thị hộp thoại cho người dùng chọn "Ghi đè" hoặc "Thêm"
//        val file = File(directory, fileName)
//        AlertDialog.Builder(context)
//            .setTitle("Lưu tệp")
//            .setMessage("Bạn có muốn ghi đè tệp hiện có không?")
//            .setPositiveButton("Ghi đè") { _, _ ->
//                saveFile(inputStream, file)
//                Toast.makeText(requireContext(), "File overwritten: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
//            }
//            .setNegativeButton("Không") { _, _ ->
//                val newFile = getUniqueFileName(directory, fileName)
//                saveFile(inputStream, newFile)
//                Toast.makeText(requireContext(), "File saved: ${newFile.absolutePath}", Toast.LENGTH_SHORT).show()
//            }
//            .show()
//    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    }



    private fun loadUIRefPosition(){
        binding.imageName.text = listWall[position].name
        setFavorite()
        Glide.with(this).load(listWall[position].url).into(binding.fullscreenImageView)
    }

    private fun setFavorite(){
        if(listWall[position].isFavorite){
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_36)
        }else{
            binding.favoriteButton.setImageResource(R.drawable.baseline_favorite_border_36)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}