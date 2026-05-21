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
fun DashboardScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF8D6E63), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color.White)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text("7 Day Streak!", fontWeight = FontWeight.Bold)
                        Text("You're crushing your daily goals. Keep the momentum going!", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Today's Energy", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(24.dp))

                    // Donut Chart Placeholder
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = 0.54f,
                            modifier = Modifier.size(200.dp),
                            strokeWidth = 20.dp,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color(0xFFEEEEEE)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("1,240", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Text("CAL LEFT", style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Consumed", style = MaterialTheme.typography.labelMedium)
                            Text("1,060", fontWeight = FontWeight.Bold)
                        }
                        Divider(modifier = Modifier.height(40.dp).width(1.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Goal", style = MaterialTheme.typography.labelMedium)
                            Text("2,300", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Text("Macronutrients", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            MacroCard("Protein", "85g / 140g", 0.6f, MaterialTheme.colorScheme.primary)
            MacroCard("Carbs", "150g / 220g", 0.7f, MaterialTheme.colorScheme.primary)
            MacroCard("Fats", "32g / 65g", 0.5f, Color(0xFFFFA726))
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun MacroCard(label: String, value: String, progress: Float, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(label, fontWeight = FontWeight.Medium)
                Text(value, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = color,
                trackColor = Color(0xFFEEEEEE)
            )
        }
    }
}