package com.example.fuelfinder.ui.main

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

class MainViewModel(private val repository: FuelRepository) : ViewModel() {

    private val _stations = MutableStateFlow<Resource<List<Station>>>(Resource.Loading)
    val stations: StateFlow<Resource<List<Station>>> = _stations.asStateFlow()

    private val _selectedFuelType = MutableStateFlow(FuelType.GASOLINE)
    val selectedFuelType: StateFlow<FuelType> = _selectedFuelType.asStateFlow()

    init {
        // Fetch default city initially
        fetchStations("Lisbon")
    }

    fun fetchStations(city: String) {
        viewModelScope.launch {
            repository.getStations(city).collect { result ->
                _stations.value = result
            }
        }
    }

    fun setFuelType(type: FuelType) {
        _selectedFuelType.value = type
    }
}

class MainViewModelFactory(private val repository: FuelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
