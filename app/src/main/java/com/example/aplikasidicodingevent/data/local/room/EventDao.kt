package com.example.aplikasidicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.aplikasidicodingevent.data.local.entity.FavoriteEvent
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(event: FavoriteEvent)

    @Update
    suspend fun update(favorite: FavoriteEvent)

    @Delete
    suspend fun deleteFavorite(event: FavoriteEvent)

    @Query("SELECT * FROM favorite_events")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEvent>>

    @Query("SELECT * FROM favorite_events WHERE id = :id")
    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent?>
}