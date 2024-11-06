package com.example.aplikasidicodingevent.data.retrofit

import com.example.aplikasidicodingevent.data.response.DetailEventResponse
import com.example.aplikasidicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getActiveEvents(
        @Query("active") active: Int
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<DetailEventResponse>  // Ubah tipe response
}