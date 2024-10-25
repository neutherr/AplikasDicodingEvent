package com.example.aplikasidicodingevent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasidicodingevent.data.response.EventResponse
import com.example.aplikasidicodingevent.data.response.ListEventsItem
import com.example.aplikasidicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _detailEvent = MutableLiveData<ListEventsItem>()
    val detailEvent: LiveData<ListEventsItem> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailEvent(id: Int) {
        _isLoading.value = true
        Log.d(TAG, "Fetching detail for event ID: $id")

        val client = ApiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                Log.d(TAG, "Response received: ${response.body()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error!!) {
                        try {
                            // Ambil event dari response dan set ke LiveData
                            responseBody.listEvents.firstOrNull()?.let { event ->
                                _detailEvent.value = event
                                Log.d(TAG, "Detail event set successfully: $event")
                            } ?: run {
                                Log.e(TAG, "Event data is null")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error processing event data", e)
                        }
                    } else {
                        Log.e(TAG, "Response error: ${responseBody?.message}")
                    }
                } else {
                    Log.e(TAG, "Response not successful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Network error: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "com.example.aplikasidicodingevent.ui.detail.DetailViewModel"
    }
}