package com.example.ringtuneandwallpaper.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.model.RingtoneApi
import com.example.ringtuneandwallpaper.model.RingtoneEntity
import com.example.ringtuneandwallpaper.ui.view.RingToneFragmentDirections
import com.example.ringtuneandwallpaper.viewmodel.ShareViewModel

class RingtoneAdapter(
    private val viewModel: ShareViewModel?,
    private val dataset: List<RingtoneEntity>,
    private val navController: NavController
): RecyclerView.Adapter<RingtoneAdapter.RingtoneViewHolder>() {
    class RingtoneViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val ringtoneNameView: TextView = view.findViewById(R.id.ringtoneName)
        val ringtoneTimeView: TextView = view.findViewById(R.id.ringtoneTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingtoneViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_ringtone, parent, false)
        return RingtoneViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: RingtoneViewHolder, position: Int) {
        var index = position
        Log.e("Vigelos", "onBindViewHolder: $index")
        // Favorite or search list
        if(viewModel?.ringtoneList?.value != null){
            index = viewModel.ringtoneList.value!!.indexOf(dataset[position])
        }
        holder.ringtoneNameView.text = dataset[position].name
        holder.ringtoneTimeView.text = "3:50"
        holder.ringtoneNameView.setOnClickListener {
            val action = RingToneFragmentDirections.actionRingTuneFragmentToPlayerMusicFragment(index)
            navController.navigate(action)
        }
    }
}