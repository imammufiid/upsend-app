package com.mufiid.up_send.ui.participant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mufiid.up_send.R
import com.mufiid.up_send.data.EventEntity
import com.mufiid.up_send.data.UserEntity
import kotlinx.android.synthetic.main.item_participant.view.*

class ParticipantAdapter :
    RecyclerView.Adapter<ParticipantAdapter.ViewHolder>() {

    var data = ArrayList<UserEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_participant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantAdapter.ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserEntity) {
            itemView.tv_username.text = user.username
        }
    }

    fun setParticipant(user: List<UserEntity>) {
        data.clear()
        data.addAll(user)
        notifyDataSetChanged()
    }
}