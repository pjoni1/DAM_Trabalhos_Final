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
fun LogFoodScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for food...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = { Icon(Icons.Default.Mic, null) },
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SuggestionChip(onClick = {}, label = { Text("Frequent") }, icon = { Icon(Icons.Default.Whatshot, null, Modifier.size(18.dp)) })
            SuggestionChip(onClick = {}, label = { Text("Recent") }, icon = { Icon(Icons.Default.History, null, Modifier.size(18.dp)) })
            SuggestionChip(onClick = {}, label = { Text("Favorites") }, icon = { Icon(Icons.Default.FavoriteBorder, null, Modifier.size(18.dp)) })
        }

        Spacer(Modifier.height(24.dp))
        Text("Manual Entry", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Food Name", style = MaterialTheme.typography.labelLarge)
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("e.g., Grilled Chicken Breast") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )
            }
        }

        Spacer(Modifier.weight(1f))
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.AddCircleOutline, null)
            Spacer(Modifier.width(8.dp))
            Text("Log Food")
        }
        Spacer(Modifier.height(80.dp))
    }
}