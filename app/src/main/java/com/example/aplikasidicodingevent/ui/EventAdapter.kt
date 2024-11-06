package com.example.aplikasidicodingevent.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
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

                // Modifikasi di sini untuk navigasi yang benar
                root.setOnClickListener {
                    val bundle = Bundle().apply {
                        putInt("eventId", event.id ?: 0)
                    }
                    Navigation.findNavController(itemView)
                        .navigate(R.id.navigation_detail, bundle,
                            // Tambahkan ini untuk mengatur behavior navigasi
                            NavOptions.Builder()
                                .setPopUpTo(R.id.navigation_favorite, false)
                                .build()
                        )
                }
            }
        }
    }
}