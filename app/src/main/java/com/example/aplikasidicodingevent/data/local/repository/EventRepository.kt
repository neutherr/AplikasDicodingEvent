package com.example.aplikasidicodingevent.data.local.repository

import androidx.lifecycle.LiveData
import com.example.aplikasidicodingevent.data.local.entity.FavoriteEvent
import com.example.aplikasidicodingevent.data.local.room.EventDao

class EventRepository private constructor(
    private val mFavDao: EventDao,
) {
    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent?> {
        return mFavDao.getFavoriteEventById(id)
    }

    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>> {
        return mFavDao.getAllFavoriteEvents()
    }

    suspend fun addToFavorite(event: FavoriteEvent) {
        mFavDao.insertFavorite(event)
    }

    suspend fun removeFromFavorite(event: FavoriteEvent) {
        mFavDao.deleteFavorite(event)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(
            mFavDao: EventDao
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(mFavDao)
            }.also { instance = it }
    }
}