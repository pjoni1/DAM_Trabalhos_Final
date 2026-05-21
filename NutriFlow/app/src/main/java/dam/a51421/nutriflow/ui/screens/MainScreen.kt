package dam.a51421.nutriflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dam.a51421.nutriflow.ui.components.NutriFlowScaffold

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf("Dashboard") }

    NutriFlowScaffold(
        title = currentScreen,
        currentScreen = currentScreen,
        onNavigate = { screen ->
            currentScreen = screen
            navController.navigate(screen.lowercase().replace(" ", ""))
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") { DashboardScreen() }
            composable("mealplan") { MealPlanScreen() }
            composable("vault") { VaultScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}