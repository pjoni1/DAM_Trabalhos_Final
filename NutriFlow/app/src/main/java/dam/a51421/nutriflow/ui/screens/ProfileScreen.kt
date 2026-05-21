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
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.primary, CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Science, null, tint = Color.White, modifier = Modifier.size(40.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text("Biometrics", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineLarge)
        Text("Update your vital stats to recalibrate.", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Physical Metrics", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = "28", onValueChange = {}, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = "75.5", onValueChange = {}, label = { Text("Weight (kg)") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = "180", onValueChange = {}, label = { Text("Height (cm)") }, modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Icon(Icons.Default.Save, null)
            Spacer(Modifier.width(8.dp))
            Text("Update Profile")
        }
        Spacer(Modifier.height(80.dp))
    }
}