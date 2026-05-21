package com.example.cooljetpackweatherapp.ui

data class WeatherUIState(
    val latitude: String = "38.7223",
    val longitude: String = "-9.1393",
    val temperature: Float = 0f,
    val windspeed: Float = 0f,
    val winddirection: Int = 0,
    val weathercode: Int = 0,
    val seaLevelPressure: Float = 0f,
    val time: String = "--:--",
    val isDay: Boolean = true
)