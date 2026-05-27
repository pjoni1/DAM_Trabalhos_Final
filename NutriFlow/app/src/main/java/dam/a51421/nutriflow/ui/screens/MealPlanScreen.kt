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

@Composable
fun MealPlanScreen(viewModel: NutriFlowViewModel) {
    val mealPlan by viewModel.currentMealPlan.collectAsState()
    val entries by viewModel.mealEntries.collectAsState()

    // Data de hoje formatada
    val todayDateString = remember {
        val sdf = SimpleDateFormat("dd 'de' MMMM, yyyy", Locale.forLanguageTag("pt-PT"))
        sdf.format(Date())
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
                    IconButton(onClick = {}) { Icon(Icons.Default.ChevronLeft, null) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "HOJE", 
                            color = MaterialTheme.colorScheme.primary, 
                            fontWeight = FontWeight.Bold, 
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = todayDateString, 
                            fontWeight = FontWeight.Bold, 
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    IconButton(onClick = {}) { Icon(Icons.Default.ChevronRight, null) }
                }
            }
        }

        // Seções de Refeições dinâmicas
        val targetFoods = mealPlan?.targetFoods ?: emptyList()
        
        if (targetFoods.isEmpty()) {
            item {
                Text(
                    "Nenhum plano alimentar disponível para hoje.",
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
                    MealSection("Pequeno-Almoço", "08:00 AM", breakfast, entries, viewModel)
                }
            }
            if (lunch.isNotEmpty()) {
                item {
                    MealSection("Almoço", "12:30 PM", lunch, entries, viewModel)
                }
            }
            if (dinner.isNotEmpty()) {
                item {
                    MealSection("Jantar", "08:00 PM", dinner, entries, viewModel)
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
    viewModel: NutriFlowViewModel
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
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(
                            if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.background,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
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
                            if (checked) {
                                viewModel.logPlanFood(food)
                            } else {
                                viewModel.removePlanFood(food)
                            }
                        },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = food.name, 
                            fontWeight = FontWeight.SemiBold,
                            color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${food.calories} kcal | P: ${food.protein}g | H: ${food.carbs}g | G: ${food.fats}g",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
