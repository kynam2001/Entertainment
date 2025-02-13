package com.example.ringtuneandwallpaper.ui.view

import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.databinding.FragmentRingtoneDetailBinding
import com.example.ringtuneandwallpaper.utility.navigateBack
import com.example.ringtuneandwallpaper.utility.navigateForward

@RequiresApi(Build.VERSION_CODES.Q)
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
            findNavController().navigateForward(action)
        }
        binding.ringtoneName.text = listRing[position].name.replace("_"," ")
        binding.duration.text = "Duration: ${formatSecondsToMinutes(listRing[position].duration)}"
        binding.setRingtoneButton.setOnClickListener {
            getFileUri(listRing[position].name)
        }
    }

    private fun getFileUri(fileName: String) {
        val uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Downloads._ID,
            MediaStore.Downloads.DISPLAY_NAME)
        val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ?"
        Log.e("Vigelos", "File name: $fileName")
        val selectionArgs = arrayOf("${fileName}.mp3")  // Thay bằng tên tệp đã xử lý

        val cursor = requireContext().contentResolver.query(uri, projection, selection, selectionArgs, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val fileUri = ContentUris.withAppendedId(uri, it.getLong(it.getColumnIndexOrThrow(MediaStore.Downloads._ID)))
                moveFileToAlarmsFolder(fileUri, fileName)
            }
            else{
                Toast.makeText(requireContext(), "Tệp chưa được download", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveFileToAlarmsFolder(fileUri: Uri, fileName: String) {
        val resolver = requireContext().contentResolver

        // Tạo metadata cho tệp trong thư mục Alarms
        val contentValues = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)  // Tên tệp
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg")  // Loại MIME
            put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_ALARMS)  // Thư mục Alarms
            put(MediaStore.Audio.Media.IS_ALARM, true)  // Đánh dấu là âm báo thức
            put(MediaStore.Audio.Media.IS_RINGTONE, false)
            put(MediaStore.Audio.Media.IS_NOTIFICATION, false)
        }

        // Chèn tệp vào MediaStore trong thư mục Alarms
        val alarmsUri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)

        alarmsUri?.let {
            // Mở InputStream từ URI của tệp trong thư mục Downloads
            val inputStream = resolver.openInputStream(fileUri)

            // Mở OutputStream tới thư mục Alarms
            resolver.openOutputStream(it).use { outputStream ->
                inputStream?.copyTo(outputStream!!)
            }

            Toast.makeText(requireContext(), "Tệp đã di chuyển vào thư mục Alarms", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(requireContext(), "Di chuyển tệp thất bại", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatSecondsToMinutes(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

}