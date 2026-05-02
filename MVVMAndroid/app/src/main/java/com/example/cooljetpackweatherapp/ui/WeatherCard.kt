package com.example.cooljetpackweatherapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeatherCard(
    temperature: Float,
    windSpeed: Float,
    pressure: Float,
    time: String
) {
    Card(
        modifier = Modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            WeatherRow(label = "Temperature", value = "$temperature °C")
            WeatherRow(label = "Wind Speed", value = "$windSpeed km/h")
            WeatherRow(label = "Pressure", value = "$pressure hPa")
            WeatherRow(label = "Time", value = time)
        }
    }
}