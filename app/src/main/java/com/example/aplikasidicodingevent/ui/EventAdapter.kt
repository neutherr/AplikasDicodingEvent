package com.example.aplikasidicodingevent.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasidicodingevent.R
import com.example.aplikasidicodingevent.data.response.ListEventsItem
import com.example.aplikasidicodingevent.databinding.ItemEventBinding

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private val events = mutableListOf<ListEventsItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setEvents(newEvents: List<ListEventsItem>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    inner class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.apply {
                tvTitle.text = event.name
                Glide.with(itemView.context)
                    .load(event.mediaCover)
                    .into(ivPicture)

                root.setOnClickListener {
                    // Tambahkan log untuk memeriksa ID
                    val eventId = event.id
                    Log.d("EventAdapter", "Clicked event with id: $eventId")

                    val bundle = Bundle().apply {
                        putInt("eventId", event.id ?: 0)
                    }

                    try {
                        itemView.findNavController().navigate(
                            R.id.navigation_detail,
                            bundle
                        )
                    } catch (e: Exception) {
                        Log.e("EventAdapter", "Navigation failed", e)
                    }
                }
            }
        }
    }
}


