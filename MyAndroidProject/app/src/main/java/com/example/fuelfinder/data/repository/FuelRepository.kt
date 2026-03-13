package com.example.fuelfinder.data.repository

import com.example.fuelfinder.data.api.FuelApi
import com.example.fuelfinder.data.model.FuelStation
import com.example.fuelfinder.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class FuelRepository(private val api: FuelApi) {

    fun getStations(localidade: String = "Lisboa"): Flow<Resource<List<FuelStation>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getStations(localidade)
            emit(Resource.Success(response.resultado))
        } catch (e: IOException) {
            emit(Resource.Error(e, "Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(Resource.Error(e, "An unexpected error occurred: ${e.message}"))
        }
    }
}
