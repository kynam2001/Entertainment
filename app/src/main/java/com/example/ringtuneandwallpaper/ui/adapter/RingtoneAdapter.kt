package com.example.ringtuneandwallpaper.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.model.RingtoneEntity
import com.example.ringtuneandwallpaper.ui.view.RingToneFragmentDirections

class RingtoneAdapter(
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
        holder.ringtoneNameView.text = dataset[position].name
        holder.ringtoneTimeView.text = "3:50"
        holder.ringtoneNameView.setOnClickListener {
            val action = RingToneFragmentDirections.actionRingTuneFragmentToPlayerMusicFragment(dataset.toTypedArray(), position)
            navController.navigate(action)
        }
    }
}