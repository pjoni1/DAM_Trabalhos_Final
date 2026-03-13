package com.example.fuelfinder.data.repository

import com.example.fuelfinder.data.api.FuelApi
import com.example.fuelfinder.data.model.Station
import com.example.fuelfinder.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FuelRepository(private val api: FuelApi) {

    fun getStations(city: String): Flow<Resource<List<Station>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getStations(city)
            emit(Resource.Success(response.stations))
        } catch (e: IOException) {
            emit(Resource.Error(e, "Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(Resource.Error(e, "An unexpected error occurred: ${e.message}"))
        }
    }
}
