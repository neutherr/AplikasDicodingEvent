package com.example.aplikasidicodingevent.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasidicodingevent.data.local.repository.EventRepository
import com.example.aplikasidicodingevent.data.local.room.EventDatabase
import com.example.aplikasidicodingevent.ui.detail.DetailViewModel
import com.example.aplikasidicodingevent.ui.favorite.FavoriteViewModel

class ViewModelFactory private constructor(
    private val repository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val database = EventDatabase.getInstance(context.applicationContext)
                val repository = EventRepository.getInstance(database.eventDao())
                ViewModelFactory(repository).also { INSTANCE = it }
            }
        }
    }
}