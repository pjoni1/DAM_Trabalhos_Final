package com.example.fuelfinder.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fuelfinder.data.model.Station
import com.example.fuelfinder.data.repository.FuelRepository
import com.example.fuelfinder.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: FuelRepository, private val stationId: String?) : ViewModel() {

    private val _stationState = MutableStateFlow<Resource<Station>>(Resource.Loading)
    val stationState: StateFlow<Resource<Station>> = _stationState.asStateFlow()

    init {
        fetchStationDetails()
    }

    private fun fetchStationDetails() {
        if (stationId == null) {
            _stationState.value = Resource.Error(Exception("Station ID is null"), "Invalid Station")
            return
        }

        viewModelScope.launch {
            repository.getStations("Lisbon").collect { result ->
                when (result) {
                    is Resource.Loading -> _stationState.value = Resource.Loading
                    is Resource.Success -> {
                        val station = result.data.find { it.id == stationId }
                        if (station != null) {
                            _stationState.value = Resource.Success(station)
                        } else {
                            _stationState.value = Resource.Error(Exception("Not found"), "Station not found")
                        }
                    }
                    is Resource.Error -> _stationState.value = Resource.Error(result.exception, result.message)
                }
            }
        }
    }
}

class DetailsViewModelFactory(
    private val repository: FuelRepository,
    private val stationId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(repository, stationId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
