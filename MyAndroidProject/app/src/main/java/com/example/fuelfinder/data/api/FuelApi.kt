package com.example.fuelfinder.data.api

import com.example.fuelfinder.data.model.StationsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FuelApi {
    @GET("stations")
    suspend fun getStations(
        @Query("city") city: String
    ): StationsResponse
}
