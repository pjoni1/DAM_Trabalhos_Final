package dam.a51421.nutriflow.ui.components

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
import androidx.compose.ui.res.stringResource
import dam.a51421.nutriflow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutriFlowScaffold(
    title: String,
    currentScreen: String,
    onNavigate: (String) -> Unit,
    currentLanguage: String = "pt",
    onLanguageChange: (String) -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    Icon(
                        Icons.Default.Eco,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Português (PT)") },
                                onClick = {
                                    onLanguageChange("pt")
                                    expanded = false
                                },
                                leadingIcon = { Text("🇵🇹") }
                            )
                            DropdownMenuItem(
                                text = { Text("English (EN)") },
                                onClick = {
                                    onLanguageChange("en")
                                    expanded = false
                                },
                                leadingIcon = { Text("🇬🇧") }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                val items = listOf("dashboard", "mealplan", "vault", "profile")
                val labels = listOf(stringResource(R.string.dashboard), stringResource(R.string.meal_plan), stringResource(R.string.vault), stringResource(R.string.profile))
                val icons = listOf(Icons.Default.Dashboard, Icons.Default.Checklist, Icons.Default.GridView, Icons.Default.Person)

                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = currentScreen == item,
                        onClick = { onNavigate(item) },
                        icon = { Icon(icons[index], contentDescription = labels[index]) },
                        label = { Text(labels[index]) }
                    )
                }
            }
        },
        floatingActionButton = floatingActionButton,
        content = content
    )
}