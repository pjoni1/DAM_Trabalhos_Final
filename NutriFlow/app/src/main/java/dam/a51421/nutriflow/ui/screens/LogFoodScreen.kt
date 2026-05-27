package dam.a51421.nutriflow.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam.a51421.nutriflow.ui.viewmodel.NutriFlowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogFoodScreen(
    viewModel: NutriFlowViewModel,
    onNavigateBack: () -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Alimentos sugestivos rápidos
    val suggestions = listOf(
        QuickFood("Maçã", 52, 0, 14, 0, 100.0),
        QuickFood("Banana", 89, 1, 23, 0, 100.0),
        QuickFood("Peito de Frango", 165, 31, 0, 3, 100.0),
        QuickFood("Arroz Branco", 130, 2, 28, 0, 100.0),
        QuickFood("Iogurte Grego", 59, 10, 3, 0, 100.0)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Log Food", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Caixa de Pesquisa rápida (Simulada)
            item {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    enabled = false, // Simulada por agora
                    modifier = Modifier.fillMaxWidth().clickable {
                        // Implementaremos o OpenFoodFacts/Barcode scanner na Fase 4!
                    },
                    placeholder = { Text("Pesquisar na base de dados...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = { Icon(Icons.Default.QrCodeScanner, null, tint = MaterialTheme.colorScheme.primary) },
                    shape = RoundedCornerShape(24.dp)
                )
            }

            // Atalhos rápidos
            item {
                Text("Sugestões Rápidas (Toque para preencher)", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    suggestions.take(3).forEach { food ->
                        SuggestionChip(
                            onClick = {
                                foodName = food.name
                                calories = food.calories.toString()
                                protein = food.protein.toString()
                                carbs = food.carbs.toString()
                                fats = food.fats.toString()
                                quantity = food.defaultQuantity.toInt().toString()
                            },
                            label = { Text(food.name) },
                            icon = { Icon(Icons.Default.Whatshot, null, Modifier.size(16.dp)) }
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    suggestions.drop(3).forEach { food ->
                        SuggestionChip(
                            onClick = {
                                foodName = food.name
                                calories = food.calories.toString()
                                protein = food.protein.toString()
                                carbs = food.carbs.toString()
                                fats = food.fats.toString()
                                quantity = food.defaultQuantity.toInt().toString()
                            },
                            label = { Text(food.name) },
                            icon = { Icon(Icons.Default.Whatshot, null, Modifier.size(16.dp)) }
                        )
                    }
                }
            }

            // Formulário Manual
            item {
                Text("Registo Manual", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = foodName,
                            onValueChange = { foodName = it },
                            label = { Text("Nome do Alimento") },
                            placeholder = { Text("ex: Ovos Mexidos") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = calories,
                                onValueChange = { calories = it },
                                label = { Text("Calorias (kcal)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = quantity,
                                onValueChange = { quantity = it },
                                label = { Text("Qtd (g / ml)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Text("Macronutrientes (Opcional)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = protein,
                                onValueChange = { protein = it },
                                label = { Text("Prot (g)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = carbs,
                                onValueChange = { carbs = it },
                                label = { Text("HC (g)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = fats,
                                onValueChange = { fats = it },
                                label = { Text("Lip (g)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Exibição de erro
            errorMessage?.let {
                item {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            // Botão de Registar
            item {
                Button(
                    onClick = {
                        val caloriesVal = calories.toIntOrNull()
                        val quantityVal = quantity.toDoubleOrNull() ?: 100.0
                        val proteinVal = protein.toIntOrNull() ?: 0
                        val carbsVal = carbs.toIntOrNull() ?: 0
                        val fatsVal = fats.toIntOrNull() ?: 0

                        if (foodName.trim().isEmpty()) {
                            errorMessage = "Por favor, introduz o nome do alimento."
                        } else if (caloriesVal == null || caloriesVal < 0) {
                            errorMessage = "Por favor, introduz um valor de calorias válido."
                        } else {
                            viewModel.logManualFood(
                                name = foodName,
                                calories = caloriesVal,
                                protein = proteinVal,
                                carbs = carbsVal,
                                fats = fatsVal,
                                quantity = quantityVal
                            )
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.AddCircleOutline, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Registar Alimento")
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

data class QuickFood(
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val defaultQuantity: Double
)