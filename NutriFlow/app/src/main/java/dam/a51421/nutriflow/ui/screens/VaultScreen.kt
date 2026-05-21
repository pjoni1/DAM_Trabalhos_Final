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
fun VaultScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = true, onClick = {}, label = { Text("Evolução Física") }, leadingIcon = { Icon(Icons.Default.FitnessCenter, null) })
            FilterChip(selected = false, onClick = {}, label = { Text("Fotos de Refeições") }, leadingIcon = { Icon(Icons.Default.Restaurant, null) })
        }

        Spacer(Modifier.height(24.dp))
        Text("Recent Progress", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
        Text("Track your physical transformation.", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(3) { index ->
                Box(modifier = Modifier.aspectRatio(0.8f).background(Color.LightGray, RoundedCornerShape(16.dp))) {
                    if (index == 2) {
                        Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Lock, null, tint = Color.Gray)
                            Text("Month 3", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}