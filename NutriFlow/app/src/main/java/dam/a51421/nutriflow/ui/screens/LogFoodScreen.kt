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
    
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by viewModel.foodSearchResults.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.seedFoodDatabase()
    }

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
            // Caixa de Pesquisa e Autocomplete
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { 
                        searchQuery = it
                        viewModel.searchFoods(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Pesquisar na base de dados...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = { 
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { 
                                searchQuery = ""
                                viewModel.searchFoods("") 
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar pesquisa")
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp)
                )

                if (searchResults.isNotEmpty() && searchQuery.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column {
                            searchResults.forEach { food ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            foodName = food.name
                                            calories = food.calories.toString()
                                            protein = food.protein.toString()
                                            carbs = food.carbs.toString()
                                            fats = food.fats.toString()
                                            quantity = food.defaultQuantity.toInt().toString()
                                            
                                            searchQuery = ""
                                            viewModel.searchFoods("")
                                        }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(Modifier.width(12.dp))
                                    Column {
                                        Text(food.name, fontWeight = FontWeight.SemiBold)
                                        Text("${food.calories} kcal | ${food.defaultQuantity.toInt()}g", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    }
                                }
                                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                            }
                        }
                    }
                }
            }

            // Botão de Pesquisa com Câmara
            item {
                Button(
                    onClick = { /* Implementação futura com API de Visão */ },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Câmara")
                    Spacer(Modifier.width(8.dp))
                    Text("Pesquisar com Câmara", fontWeight = FontWeight.Bold)
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
