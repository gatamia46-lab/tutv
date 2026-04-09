package com.example.tutv

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VideoAdapter(
    private val videos: List<VideoModel>,
    private val onItemClick: (VideoModel) -> Unit,
    private val onItemFocus: (VideoModel) -> Unit
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreCanal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]
        holder.txtNombre.text = video.nombre

        holder.itemView.isFocusable = true
        holder.itemView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.setBackgroundColor(Color.parseColor("#33FFFFFF"))
                holder.txtNombre.setTextColor(Color.parseColor("#FFD700"))
                onItemFocus(video)
            } else {
                view.setBackgroundColor(Color.TRANSPARENT)
                holder.txtNombre.setTextColor(Color.WHITE)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(video)
        }
    }

    override fun getItemCount() = videos.size
}
