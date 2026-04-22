package dam.A51421.coolweatherapp

import android.Manifest
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    var day: Boolean = true
    private lateinit var fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        day = hour in 7..18

        if (day) {
            setTheme(R.style.Theme_Day)
        } else {
            setTheme(R.style.Theme_Night)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this)

        val btnUpdate: android.widget.Button? = findViewById(R.id.btnUpdate)
        btnUpdate?.setOnClickListener {
            updateWithCurrentLabels()
        }

        checkLocationPermissions()
    }

    private fun checkLocationPermissions() {
        if (androidx.core.app.ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            androidx.core.app.ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            updateWithCurrentLabels()
        } else {
            getInitialLocation()
        }
    }

    private fun updateWithCurrentLabels() {
        val latLabel: TextView = findViewById(R.id.latLabel)
        val longLabel: TextView = findViewById(R.id.longLabel)

        val lat = latLabel.text.toString().toFloatOrNull() ?: 0.0f
        val long = longLabel.text.toString().toFloatOrNull() ?: 0.0f

        fetchWeatherData(lat, long).start()
    }

    private fun fetchWeatherData ( lat : Float , long : Float ) : Thread {
        return Thread {
            val weather = WeatherAPI_Call ( lat , long )
            updateUI ( weather )
        }
    }

    private fun updateUI(request: WeatherData) {
        runOnUiThread {
            val weatherImage: ImageView = findViewById(R.id.weatherImage)
            val tempLabel: TextView = findViewById(R.id.tempValue)
            val pressure: TextView = findViewById(R.id.pressureValue)

            val mainLayout: androidx.constraintlayout.widget.ConstraintLayout = findViewById(R.id.container)

            val isDayInLocation = request.current_weather.is_day == 1
            this.day = isDayInLocation

            if (isDayInLocation) {
               mainLayout.setBackgroundResource(R.drawable.sunny_bg)
            } else {
                mainLayout.setBackgroundResource(R.drawable.night_bg)
            }

            this.day = isDayInLocation

            val currentTemp = request.current_weather.temperature
            tempLabel.text = "$currentTemp°C"

            val currentPressure = request.hourly.pressure_msl.get(12)
            pressure.text = "$currentPressure hPa"

            val mapt = getWeatherCodeMap()
            val wCode = mapt.get(request.current_weather.weathercode)

            val wImage = when (wCode) {
                WMO_WeatherCode.CLEAR_SKY,
                WMO_WeatherCode.MAINLY_CLEAR,
                WMO_WeatherCode.PARTLY_CLOUDY -> if (isDayInLocation) "${wCode?.image}day" else "${wCode?.image}night"
                else -> wCode?.image
            }

            val resID = resources.getIdentifier(wImage, "drawable", packageName)
            if (resID != 0) {
                weatherImage.setImageDrawable(getDrawable(resID))
            }
        }
    }
    private fun WeatherAPI_Call ( lat : Float , long : Float ) : WeatherData {
        12
        val reqString = buildString {
            append ("https://api.open-meteo.com/v1/forecast?")
            append ("latitude=${lat}&longitude=${long}&")
            append ("current_weather=true&")
            append ("hourly=temperature_2m,weathercode,pressure_msl,windspeed_10m")
        }
        val str = reqString . toString ()
        val url = URL(reqString.toString());
        url . openStream () . use {
            val request = Gson(). fromJson (InputStreamReader(it, "UTF-8"), WeatherData :: class.java )
            return request
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun getInitialLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude.toFloat()
                val long = location.longitude.toFloat()

                findViewById<TextView>(R.id.latLabel).text = lat.toString()
                findViewById<TextView>(R.id.longLabel).text = long.toString()

                fetchWeatherData(lat, long).start()
            }
        }
    }
}