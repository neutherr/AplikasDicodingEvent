package com.example.aplikasidicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.aplikasidicodingevent.data.local.repository.EventRepository
import com.example.aplikasidicodingevent.data.response.ListEventsItem

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {
    val favoriteEvents: LiveData<List<ListEventsItem>> = repository.getAllFavoriteEvents().map { favorites ->
        favorites.map { favorite ->
            ListEventsItem(
                id = favorite.id.toInt(),
                name = favorite.name,
                mediaCover = favorite.mediaCover
            )
        }
    }
}