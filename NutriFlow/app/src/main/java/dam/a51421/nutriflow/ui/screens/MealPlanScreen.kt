package dam.a51421.nutriflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam.a51421.nutriflow.data.model.TargetFood
import dam.a51421.nutriflow.ui.viewmodel.NutriFlowViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.res.stringResource
import dam.a51421.nutriflow.R

@Composable
fun MealPlanScreen(viewModel: NutriFlowViewModel) {
    val mealPlan by viewModel.currentMealPlan.collectAsState()
    val entries by viewModel.filteredMealEntries.collectAsState()
    val offset by viewModel.selectedDateOffset.collectAsState()

    val displayDate = remember(offset) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, offset)
        SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(cal.time)
    }

    val label = when (offset) {
        0 -> stringResource(R.string.today)
        -1 -> stringResource(R.string.yesterday)
        1 -> stringResource(R.string.tomorrow)
        else -> if (offset < 0) stringResource(R.string.x_days_ago, -offset) else stringResource(R.string.in_x_days, offset)
    }

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

        // Seções de Refeições dinâmicas
        val targetFoods = mealPlan?.targetFoods ?: emptyList()
        
        if (targetFoods.isEmpty()) {
            item {
                Text(
                    stringResource(R.string.no_meals_planned),
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }
        } else {
            // Agrupar por hora ou tipo de refeição
            val breakfast = targetFoods.filter { it.time == "08:00 AM" }
            val lunch = targetFoods.filter { it.time == "12:30 PM" }
            val dinner = targetFoods.filter { it.time == "08:00 PM" }

            if (breakfast.isNotEmpty()) {
                item {
                    MealSection("Pequeno-Almoço", "08:00 AM", breakfast, entries, viewModel, offset)
                }
            }
            if (lunch.isNotEmpty()) {
                item {
                    MealSection("Almoço", "12:30 PM", lunch, entries, viewModel, offset)
                }
            }
            if (dinner.isNotEmpty()) {
                item {
                    MealSection("Jantar", "08:00 PM", dinner, entries, viewModel, offset)
                }
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun MealSection(
    title: String, 
    time: String, 
    foods: List<TargetFood>,
    loggedEntries: List<dam.a51421.nutriflow.data.model.MealEntry>,
    viewModel: NutriFlowViewModel,
    offset: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title, 
                    fontWeight = FontWeight.Bold, 
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.Schedule, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(Modifier.width(4.dp))
                Text(time, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Spacer(Modifier.height(16.dp))

            foods.forEach { food ->
                val isChecked = loggedEntries.any { it.foodName == food.name && it.type == "Plan Item" }
                val isEditable = offset == 0
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(
                            if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.background,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable(enabled = isEditable) {
                            if (isChecked) {
                                viewModel.removePlanFood(food)
                            } else {
                                viewModel.logPlanFood(food)
                            }
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (isEditable) {
                                if (checked) {
                                    viewModel.logPlanFood(food)
                                } else {
                                    viewModel.removePlanFood(food)
                                }
                            }
                        },
                        enabled = isEditable,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            disabledCheckedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            disabledUncheckedColor = Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = food.name, 
                            fontWeight = FontWeight.SemiBold,
                            color = if (!isEditable) Color.Gray else if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${food.calories} kcal | P: ${food.protein}g | H: ${food.carbs}g | G: ${food.fats}g",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (!isEditable) Color.Gray.copy(alpha = 0.6f) else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
