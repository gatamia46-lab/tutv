package com.example.tutv

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class YoutubeResult(val title: String, val videoId: String)

class SearchAdapter(
    private val items: List<YoutubeResult>,
    private val onClick: (YoutubeResult) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txt: TextView = v.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txt.text = item.title
        holder.txt.setTextColor(Color.WHITE)
        holder.txt.textSize = 13f
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}