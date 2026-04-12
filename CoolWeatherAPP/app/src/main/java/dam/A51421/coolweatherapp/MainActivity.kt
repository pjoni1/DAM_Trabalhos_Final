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

class MainActivity : AppCompatActivity() {

    var day = true
    private lateinit var fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Configurar o Tema (Sempre antes do super e do setContentView)
        if (day) {
            setTheme(R.style.Theme_Day)
        } else {
            setTheme(R.style.Theme_Night)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2. Inicializar o cliente de localização
        fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this)

        // 3. Configurar o botão de atualização
        val btnUpdate: android.widget.Button? = findViewById(R.id.btnUpdate)
        btnUpdate?.setOnClickListener {
            updateWithCurrentLabels()
        }

        // 4. Lógica de Inicialização: GPS ou Labels Padrão
        checkLocationPermissions()
    }

    // Função auxiliar para organizar o onCreate
    private fun checkLocationPermissions() {
        if (androidx.core.app.ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            // Se não temos permissão, pedimos ao utilizador
            androidx.core.app.ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            // Opcional: Chamar com valores 0.0 enquanto espera pela permissão
            updateWithCurrentLabels()
        } else {
            // Se já temos permissão, tentamos obter a localização real
            getInitialLocation()
        }
    }

    // Movemos esta função para fora do onCreate para ser acessível por todos
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
            val pressure: TextView = findViewById(R.id.pressureValue)
            val tempLabel: TextView = findViewById(R.id.tempValue)
            val pressureLabel: TextView = findViewById(R.id.pressureValue)

            val currentTemp = request.current_weather.temperature
            tempLabel.text = "$currentTemp°C"

            val currentPressure = request.hourly.pressure_msl.get(12)
            pressure.text = "$currentPressure hPa"

            val mapt = getWeatherCodeMap()
            val wCode = mapt.get(request.current_weather.weathercode)
            val wImage = when (wCode) {
                WMO_WeatherCode.CLEAR_SKY,
                WMO_WeatherCode.MAINLY_CLEAR,
                WMO_WeatherCode.PARTLY_CLOUDY -> if (day) "${wCode?.image}day" else "${wCode?.image}night"
                else -> wCode?.image
            }

            val res = resources
            val resID = res.getIdentifier(wImage, "drawable", packageName)

            if (resID != 0) {
                weatherImage.setImageDrawable(getDrawable(resID))
            } else {
                weatherImage.setImageResource(R.drawable.fog)
                android.util.Log.e("WEATHER_ERROR", "Não encontrei a imagem: $wImage")
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