package dam.a51421.nutriflow.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.rememberNavController
import dam.a51421.nutriflow.ui.components.NutriFlowScaffold
import dam.a51421.nutriflow.ui.viewmodel.NutriFlowViewModel
import androidx.compose.ui.res.stringResource
import dam.a51421.nutriflow.R

@Composable
fun MainScreen() {
    val viewModel: NutriFlowViewModel = viewModel()
    val userProfile by viewModel.userProfile.collectAsState()
    val isAuthenticated by viewModel.isUserAuthenticated.collectAsState()

    if (!isAuthenticated) {
        AuthScreen(viewModel = viewModel)
    } else if (userProfile == null) {
        OnboardingScreen(
            viewModel = viewModel,
            onComplete = {
                // Ao completar o perfil, a recomposição reativa direciona para a Dashboard
            }
        )
    } else {
        val navController = rememberNavController()
        var currentScreen by remember { mutableStateOf("dashboard") }

        val title = when (currentScreen) {
            "dashboard" -> stringResource(R.string.dashboard)
            "mealplan" -> stringResource(R.string.meal_plan)
            "vault" -> stringResource(R.string.vault)
            "profile" -> stringResource(R.string.profile)
            "logfood" -> stringResource(R.string.log_food)
            else -> ""
        }

        NutriFlowScaffold(
            title = title,
            currentScreen = currentScreen,
            onNavigate = { screen ->
                currentScreen = screen
                navController.navigate(screen) {
                    // Evitar acumulação de ecrãs na pilha
                    popUpTo("dashboard") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            currentLanguage = viewModel.currentLanguage.collectAsState().value,
            onLanguageChange = { viewModel.changeLanguage(it) },
            floatingActionButton = {
                if (currentScreen == "dashboard" || currentScreen == "mealplan") {
                    ExtendedFloatingActionButton(
                        onClick = {
                            currentScreen = "logfood"
                            navController.navigate("logfood")
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        icon = { Icon(Icons.Default.Add, contentDescription = "Log Food") },
                        text = { Text(stringResource(R.string.log_food)) }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "dashboard",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("dashboard") { 
                    DashboardScreen(viewModel = viewModel) 
                }
                composable("mealplan") { 
                    MealPlanScreen(viewModel = viewModel) 
                }
                composable("vault") { 
                    VaultScreen(viewModel = viewModel) 
                }
                composable("profile") { 
                    ProfileScreen(viewModel = viewModel) 
                }
                composable("logfood") {
                    LogFoodScreen(
                        viewModel = viewModel,
                        onNavigateBack = {
                            currentScreen = "dashboard"
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}