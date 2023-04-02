package com.coyotwilly.nomad

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter (
    private var destinations: List<String>,
    private var travelPeriods: List<String>,
    private var backgroundImages: List<Bitmap> ) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>(){
    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var destinationItem: TextView = itemView.findViewById(R.id.destination)
        var travelPeriodItem: TextView = itemView.findViewById(R.id.travel_period)
        var backgroundImageItem: ImageView = itemView.findViewById(R.id.card_banner)

        init {
            backgroundImageItem.setOnClickListener {
                val position = adapterPosition
                Log.d("CLICKED_ITEM", "You clicked on item #${position + 1}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false))
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.destinationItem.text = destinations[position]
        holder.travelPeriodItem.text = travelPeriods[position]
        holder.backgroundImageItem.setImageBitmap(backgroundImages[position])
    }

    override fun getItemCount(): Int {
        return destinations.size
    }
}