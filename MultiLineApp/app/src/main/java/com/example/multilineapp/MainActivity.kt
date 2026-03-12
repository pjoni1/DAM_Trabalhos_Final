package com.example.multilineapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val manufacturer = android.os.Build.MANUFACTURER
        val model = android.os.Build.MODEL
        val brand = android.os.Build.BRAND
        val type = android.os.Build.TYPE
        val user = android.os.Build.USER
        val base = android.os.Build.VERSION.BASE_OS
        val incremental = android.os.Build.VERSION.INCREMENTAL
        val sdk = android.os.Build.VERSION.SDK_INT
        val versionCode = android.os.Build.VERSION.RELEASE
        val display = android.os.Build.DISPLAY


        val texto =
                "Manufacturer: $manufacturer\n" +
                "Model: $model\n" +
                "Brand: $brand\n" +
                "Type: $type\n" +
                "User: $user\n" +
                "Base: $base\n" +
                "Incremental: $incremental\n" +
                "SDK: $sdk\n" +
                "Version Code: $versionCode\n" +
                "Display: $display"


        findViewById<EditText>(R.id.editTextTextMultiLine).setText(texto)


    }
}