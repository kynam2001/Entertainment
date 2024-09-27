package com.example.ringtuneandwallpaper.ui.view

import android.app.AlarmManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.AlarmClock
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ringtuneandwallpaper.databinding.FragmentRingtoneDetailBinding
import java.io.File

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
        binding.setRingtoneButton.setOnClickListener {
//            val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), listRing[position].name)
//            val uri = saveFileToAlarmsDirectory(file)
//            setAlarmSound(uri)
        }
    }

    private fun saveFileToAlarmsDirectory(file: File): Uri {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_ALARMS)
        }

        val resolver = requireContext().contentResolver
        val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)

        resolver.openOutputStream(uri!!).use { outputStream ->
            file.inputStream().use { inputStream ->
                inputStream.copyTo(outputStream!!)
            }
        }

        return uri
    }

    private fun setAlarmSound(alarmUri: Uri) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_RINGTONE, alarmUri.toString())
            putExtra(AlarmClock.EXTRA_MESSAGE, "Wake Up!")
            putExtra(AlarmClock.EXTRA_HOUR, 7)
            putExtra(AlarmClock.EXTRA_MINUTES, 30)
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            requireContext().startActivity(intent)
        }
    }
}