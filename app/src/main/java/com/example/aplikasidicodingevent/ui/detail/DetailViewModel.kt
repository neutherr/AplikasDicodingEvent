package com.example.aplikasidicodingevent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasidicodingevent.data.local.entity.FavoriteEvent
import com.example.aplikasidicodingevent.data.response.DetailEventResponse
import com.example.aplikasidicodingevent.data.response.ListEventsItem
import com.example.aplikasidicodingevent.data.retrofit.ApiConfig
import com.example.aplikasidicodingevent.data.local.repository.EventRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: EventRepository) : ViewModel() {
    private var _detailEvent = MutableLiveData<ListEventsItem>()
    val detailEvent: LiveData<ListEventsItem> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun getDetailEvent(eventId: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(eventId)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { detailResponse ->
                        if (!detailResponse.error!!) {
                            _detailEvent.value = detailResponse.event
                        } else {
                            Log.e(TAG, "Error: ${detailResponse.message}")
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFavoriteStatus(id: String): LiveData<FavoriteEvent?> {
        return repository.getFavoriteEventById(id)
    }

    fun toggleFavorite(event: FavoriteEvent, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                repository.removeFromFavorite(event)
            } else {
                repository.addToFavorite(event)
            }
        }
    }
}