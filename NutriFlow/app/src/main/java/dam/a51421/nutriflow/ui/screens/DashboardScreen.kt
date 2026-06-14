package dam.a51421.nutriflow.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam.a51421.nutriflow.ui.viewmodel.NutriFlowViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.ui.res.stringResource
import dam.a51421.nutriflow.R

@Composable
fun DashboardScreen(viewModel: NutriFlowViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val entries by viewModel.filteredMealEntries.collectAsState()
    val offset by viewModel.selectedDateOffset.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    val displayDate = remember(offset, currentLanguage) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, offset)
        val locale = Locale(currentLanguage)
        SimpleDateFormat("dd MMMM, yyyy", locale).format(cal.time)
    }

    val label = when (offset) {
        0 -> stringResource(R.string.today)
        -1 -> stringResource(R.string.yesterday)
        1 -> stringResource(R.string.tomorrow)
        else -> if (offset < 0) stringResource(R.string.x_days_ago, -offset) else stringResource(R.string.in_x_days, offset)
    }

    // Cálculos dinâmicos
    val calorieGoal = profile?.dailyCalorieGoal ?: 2000
    val totalCaloriesConsumed = entries.sumOf { it.calories }
    val caloriesLeft = (calorieGoal - totalCaloriesConsumed).coerceAtLeast(0)
    
    val proteinGoal = profile?.dailyProteinGoal ?: 150
    val carbsGoal = profile?.dailyCarbsGoal ?: 200
    val fatsGoal = profile?.dailyFatsGoal ?: 60

    val totalProteinConsumed = entries.sumOf { it.protein }
    val totalCarbsConsumed = entries.sumOf { it.carbs }
    val totalFatsConsumed = entries.sumOf { it.fats }

    val calorieProgress = if (calorieGoal > 0) {
        (totalCaloriesConsumed.toFloat() / calorieGoal).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = calorieProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "donut_progress"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Cabeçalho de data
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.changeDateOffset(-1) }) { Icon(Icons.Default.ChevronLeft, null) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = label, 
                            color = MaterialTheme.colorScheme.primary, 
                            fontWeight = FontWeight.Bold, 
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = displayDate, 
                            fontWeight = FontWeight.Bold, 
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    IconButton(onClick = { viewModel.changeDateOffset(1) }) { Icon(Icons.Default.ChevronRight, null) }
                }
            }
        }

        // Cartão de Streak (Estilo Duolingo)
        item {
            val streak = profile?.streakCount ?: 0
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (streak > 0) Color(0xFFFF9800) else Color.LightGray, 
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.LocalFireDepartment, 
                            contentDescription = "Streak", 
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = if (streak > 0) stringResource(R.string.days_streak, streak) else stringResource(R.string.start_your_streak), 
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = if (streak > 0) stringResource(R.string.keep_smashing) 
                                   else stringResource(R.string.log_meal_streak), 
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Donut Chart e Consumo de Energia
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val energiaTitle = when (offset) {
                        0 -> stringResource(R.string.energy_today)
                        -1 -> stringResource(R.string.energy_yesterday)
                        1 -> stringResource(R.string.energy_tomorrow)
                        else -> if (offset < 0) stringResource(R.string.energy_x_days_ago, -offset) else stringResource(R.string.energy_in_x_days, offset)
                    }
                    Text(energiaTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(24.dp))

                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.size(200.dp),
                            strokeWidth = 16.dp,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color(0xFFEEEEEE)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = String.format("%,d", caloriesLeft), 
                                fontSize = 36.sp, 
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(stringResource(R.string.calories_left), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stringResource(R.string.consumed), style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text("$totalCaloriesConsumed kcal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        VerticalDivider(modifier = Modifier.height(40.dp).width(1.dp), color = Color.LightGray)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(stringResource(R.string.daily_goal), style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text("$calorieGoal kcal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // Macronutrientes
        item {
            Text(stringResource(R.string.macronutrients), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            
            val proteinProgress = if (proteinGoal > 0) (totalProteinConsumed.toFloat() / proteinGoal).coerceIn(0f, 1f) else 0f
            val carbsProgress = if (carbsGoal > 0) (totalCarbsConsumed.toFloat() / carbsGoal).coerceIn(0f, 1f) else 0f
            val fatsProgress = if (fatsGoal > 0) (totalFatsConsumed.toFloat() / fatsGoal).coerceIn(0f, 1f) else 0f

            MacroCard(stringResource(R.string.proteins), "${totalProteinConsumed}g / ${proteinGoal}g", proteinProgress, MaterialTheme.colorScheme.primary)
            MacroCard(stringResource(R.string.carbs_short), "${totalCarbsConsumed}g / ${carbsGoal}g", carbsProgress, Color(0xFF0288D1))
            MacroCard(stringResource(R.string.fats_short), "${totalFatsConsumed}g / ${fatsGoal}g", fatsProgress, Color(0xFFFFA726))
        }

        // Registo de Refeições de Hoje
        if (entries.isNotEmpty()) {
            item {
                Spacer(Modifier.height(8.dp))
                Text("${stringResource(R.string.recent_meals)} ($label)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }

            items(entries) { entry ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(entry.foodName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "P: ${entry.protein}g | H: ${entry.carbs}g | G: ${entry.fats}g",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "${entry.calories} kcal",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            IconButton(onClick = { viewModel.removeMealEntry(entry.id) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Apagar Registo",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            item {
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = if (offset == 0) stringResource(R.string.empty_dashboard_today) 
                               else stringResource(R.string.empty_dashboard_other, label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun MacroCard(label: String, value: String, progress: Float, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(label, fontWeight = FontWeight.Medium)
                Text(value, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = color,
                trackColor = Color(0xFFEEEEEE)
            )
        }
    }
}