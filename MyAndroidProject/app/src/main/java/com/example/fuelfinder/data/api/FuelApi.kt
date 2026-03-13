package com.example.fuelfinder.data.api

import com.example.fuelfinder.data.model.DgegResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FuelApi {
    @GET("api/PrecoComb/PesquisarPostos")
    suspend fun getStations(
        @Query("localidade") localidade: String? = null
    ): DgegResponse
}
