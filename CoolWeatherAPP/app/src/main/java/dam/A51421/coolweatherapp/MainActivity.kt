package dam.A51421.coolweatherapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    var day = true

    override fun onCreate(savedInstanceState: Bundle?) {
        if (day) {
            setTheme(R.style.Theme_Day)
        } else {
            setTheme(R.style.Theme_Night)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}