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


@Composable
fun MealPlanScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {}) { Icon(Icons.Default.ChevronLeft, null) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TODAY", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                        Text("Oct 24, 2023", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    }
                    IconButton(onClick = {}) { Icon(Icons.Default.ChevronRight, null) }
                }
            }
        }

        item {
            MealSection("Breakfast", "08:00 AM", listOf("Overnight Oats with Chia" to "320 kcal", "Black Coffee" to "5 kcal"))
            Spacer(Modifier.height(16.dp))
            MealSection("Lunch", "12:30 PM", listOf("Grilled Chicken Salad" to "450 kcal", "Quinoa Side" to "110 kcal"))
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun MealSection(title: String, time: String, items: List<Pair<String, String>>) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.Schedule, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(time, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(16.dp))
            items.forEach { (name, cal) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = name.contains("Oats"), onClick = {})
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(name, fontWeight = FontWeight.Medium)
                        Text(cal, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}