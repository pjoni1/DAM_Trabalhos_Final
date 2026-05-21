package com.example.cooljetpackweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cooljetpackweatherapp.data.WeatherApiClient
import com.example.cooljetpackweatherapp.ui.WeatherUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUIState())

    val uiState: StateFlow<WeatherUIState> = _uiState.asStateFlow()

    fun updateLatitude(newLat: String) {
        _uiState.update { it.copy(latitude = newLat) }
    }

    fun updateLongitude(newLon: String) {
        _uiState.update { it.copy(longitude = newLon) }
    }

    fun fetchWeather() {
        val lat = _uiState.value.latitude.toFloatOrNull() ?: 0f
        val lon = _uiState.value.longitude.toFloatOrNull() ?: 0f

        viewModelScope.launch {
            try {
                val result = WeatherApiClient.getWeather(lat, lon)

                result?.let { data ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            temperature = data.current_weather.temperature,
                            windspeed = data.current_weather.windspeed,
                            winddirection = data.current_weather.winddirection,
                            weathercode = data.current_weather.weathercode,
                            seaLevelPressure = data.hourly.pressure_msl.firstOrNull()?.toFloat() ?: 0f,
                            isDay = data.current_weather.is_day == 1,
                            time = data.current_weather.time
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}