package dam.a51421.nutriflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dam.a51421.nutriflow.ui.screens.MainScreen
import dam.a51421.nutriflow.ui.theme.NutriFlowTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import dam.a51421.nutriflow.ui.viewmodel.NutriFlowViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: NutriFlowViewModel = viewModel()
            val currentLanguage by viewModel.currentLanguage.collectAsState()

            val context = LocalContext.current
            val registryOwner = androidx.activity.compose.LocalActivityResultRegistryOwner.current
            val backDispatcherOwner = androidx.activity.compose.LocalOnBackPressedDispatcherOwner.current
            val locale = Locale(currentLanguage)
            val configuration = Configuration(context.resources.configuration)
            configuration.setLocale(locale)
            val localizedContext = context.createConfigurationContext(configuration)

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalConfiguration provides configuration,
                androidx.activity.compose.LocalActivityResultRegistryOwner provides registryOwner!!,
                androidx.activity.compose.LocalOnBackPressedDispatcherOwner provides backDispatcherOwner!!
            ) {
                NutriFlowTheme {
                    MainScreen()
                }
            }
        }
    }
}

