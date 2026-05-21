package com.example.cooljetpackweatherapp.ui

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import com.example.cooljetpackweatherapp.viewmodel.WeatherViewModel
import com.example.cooljetpackweatherapp.data.WMO_WeatherCode
import com.example.cooljetpackweatherapp.data.getWeatherCodeMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun WeatherUI ( weatherViewModel : WeatherViewModel = viewModel () ) {
    val weatherUIState by weatherViewModel . uiState . collectAsState ()
    val latitude = weatherUIState . latitude
    val longitude = weatherUIState . longitude
    val temperature = weatherUIState . temperature
    val windSpeed = weatherUIState . windspeed
    val windDirection = weatherUIState . winddirection
    val weathercode = weatherUIState . weathercode
    val seaLevelPressure = weatherUIState . seaLevelPressure
    val time = weatherUIState . time
    val configuration = LocalConfiguration . current
    val day = weatherUIState.isDay
    val mapt = getWeatherCodeMap () ;
    val wCode = mapt . get ( weathercode )
    val wImage = when ( wCode ) {
        WMO_WeatherCode . CLEAR_SKY ,
        WMO_WeatherCode . MAINLY_CLEAR ,
        WMO_WeatherCode . PARTLY_CLOUDY -> if ( day ) wCode ?. image + "day"
        else wCode ?. image + "night"
        else -> wCode ?. image
    }
    val context = LocalContext . current
    val wIcon = context . resources . getIdentifier ( wImage , "drawable" ,
        context . packageName )
    if ( configuration . orientation == Configuration . ORIENTATION_LANDSCAPE ) {
        LandscapeWeatherUI (
            wIcon ,
            latitude ,
            longitude ,
            temperature ,
            windSpeed ,
            windDirection ,
            weathercode ,
            seaLevelPressure ,
            time ,
            onLatitudeChange = {
                    newValue ->
                weatherViewModel.updateLatitude(newValue)
            },
            onLongitudeChange = {
                    newValue ->
                weatherViewModel.updateLongitude(newValue)
            },
            onUpdateButtonClick = {
                weatherViewModel . fetchWeather ()
            }
        )
    } else {
        PortraitWeatherUI (
            wIcon ,
            latitude ,
            longitude ,
            temperature ,
            windSpeed ,
            windDirection ,
            weathercode ,
            seaLevelPressure ,
            time ,
            onLatitudeChange = {
                    newValue ->
                weatherViewModel.updateLatitude(newValue)
            },
            onLongitudeChange = {
                    newValue ->
                weatherViewModel.updateLongitude(newValue)
            },
            onUpdateButtonClick = {
                weatherViewModel . fetchWeather ()
            }
        )
    }
}

@Composable
fun PortraitWeatherUI(
    wIcon: Int,
    latitude: String,
    longitude: String,
    temperature: Float,
    windSpeed: Float,
    windDirection: Int,
    weathercode: Int,
    seaLevelPressure: Float,
    time: String,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (wIcon != 0) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = wIcon),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(120.dp)
            )
        }

        CoordinatesCard(
            latitude = latitude.toString(),
            longitude = longitude.toString(),
            onLatitudeChange = onLatitudeChange,
            onLongitudeChange = onLongitudeChange,
            onUpdateClick = onUpdateButtonClick
        )

        WeatherCard(
            temperature = temperature,
            windSpeed = windSpeed,
            pressure = seaLevelPressure,
            time = time
        )
    }
}
@Composable
fun LandscapeWeatherUI(
    wIcon: Int,
    latitude: String,
    longitude: String,
    temperature: Float,
    windSpeed: Float,
    windDirection: Int,
    weathercode: Int,
    seaLevelPressure: Float,
    time: String,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            if (wIcon != 0) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = wIcon),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }
            CoordinatesCard(
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                onLatitudeChange = onLatitudeChange,
                onLongitudeChange = onLongitudeChange,
                onUpdateClick = onUpdateButtonClick
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            WeatherCard(
                temperature = temperature,
                windSpeed = windSpeed,
                pressure = seaLevelPressure,
                time = time
            )
        }
    }
}