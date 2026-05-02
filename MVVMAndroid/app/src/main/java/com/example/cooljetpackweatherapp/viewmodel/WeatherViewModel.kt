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

    fun updateLatitude(newLat: Float) {
        _uiState.update { it.copy(latitude = newLat) }
    }

    fun updateLongitude(newLon: Float) {
        _uiState.update { it.copy(longitude = newLon) }
    }

    // 4. A função que faz o pedido à API
    fun fetchWeather() {
        val lat = _uiState.value.latitude
        val lon = _uiState.value.longitude

        // Usamos o viewModelScope para não bloquear a interface enquanto esperamos pela rede
        viewModelScope.launch {
            try {
                val result = WeatherApiClient.getWeather(lat, lon)

                result?.let { data ->
                    // Atualizamos o estado com os dados reais vindos da API
                    _uiState.update { currentState ->
                        currentState.copy(
                            temperature = data.current_weather.temperature,
                            windspeed = data.current_weather.windspeed,
                            winddirection = data.current_weather.winddirection,
                            weathercode = data.current_weather.weathercode,
                            // Vamos buscar a pressão ao primeiro elemento da lista hourly
                            seaLevelPressure = data.hourly.pressure_msl.firstOrNull()?.toFloat() ?: 0f,
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