package com.coyotwilly.nomad

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SmallViewPagerAdapter (
    private var destinations: List<String>,
    private var backgroundImages: List<Bitmap> ) : RecyclerView.Adapter<SmallViewPagerAdapter.SmallPager2ViewHolder>() {

    inner class SmallPager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var destinationItem: TextView = itemView.findViewById(R.id.upcoming_trips_memo)
        var backgroundImageItem: ImageView = itemView.findViewById(R.id.upcoming_trip_background)

        init {
            backgroundImageItem.setOnClickListener {
                val position = adapterPosition
                Log.d("CLICKED_ITEM", "You clicked on item #${position + 1}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallPager2ViewHolder {
        return SmallPager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.small_card_item, parent, false))
    }

    override fun onBindViewHolder(holder: SmallPager2ViewHolder, position: Int) {
        holder.destinationItem.text = destinations[position]
        holder.backgroundImageItem.setImageBitmap(backgroundImages[position])
    }

    override fun getItemCount(): Int {
        return destinations.size
    }
}