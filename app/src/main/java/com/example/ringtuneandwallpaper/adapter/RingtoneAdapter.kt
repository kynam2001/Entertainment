package com.example.ringtuneandwallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ringtuneandwallpaper.R
import com.example.ringtuneandwallpaper.model.Ringtone
import com.example.ringtuneandwallpaper.view.RingTuneFragmentDirections

class RingtoneAdapter(
    private val context: Context,
    private val dataset: List<Ringtone>,
    private val navController: NavController
): RecyclerView.Adapter<RingtoneAdapter.RingtoneViewHolder>() {
    class RingtoneViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.ringtoneName)
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
        val item = dataset[position]
        holder.textView.text = context.resources.getString(item.ringtoneResourceId)
        holder.textView.setOnClickListener {
            val action = RingTuneFragmentDirections.actionRingTuneFragmentToPlayerMusicFragment(item.ringtoneResourceId)
            navController.navigate(action)
        }
    }
}