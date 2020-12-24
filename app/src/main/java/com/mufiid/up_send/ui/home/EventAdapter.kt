package com.mufiid.up_send.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mufiid.up_send.R
import com.mufiid.up_send.data.EventEntity
import kotlinx.android.synthetic.main.item_event.view.*

class EventAdapter(private val onClick: (EventEntity) -> Unit): RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    val data = ArrayList<EventEntity>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(event: EventEntity) {
            itemView.title_event.text = event.name
            itemView.date_time_event.text = "${event.startDate} / ${event.dueDate}"
            when(event.status) {
                1 -> itemView.status_event.text = "Dimulai"
            }
            Glide.with(itemView)
                .load(event.image)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(itemView.image_event)
            itemView.setOnClickListener {
                onClick(event)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    fun setEvent(event: List<EventEntity>) {
        data.clear()
        data.addAll(event)
        notifyDataSetChanged()
    }
}