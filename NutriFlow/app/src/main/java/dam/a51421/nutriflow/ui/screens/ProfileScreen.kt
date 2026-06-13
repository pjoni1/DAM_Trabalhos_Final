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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import dam.a51421.nutriflow.ui.viewmodel.NutriFlowViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: NutriFlowViewModel) {
    val profile by viewModel.userProfile.collectAsState()

    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf<Long?>(null) }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var goal by remember { mutableStateOf("Maintain") }

    // Inicializar os campos quando o perfil carregar
    LaunchedEffect(profile) {
        profile?.let {
            name = it.name
            dateOfBirth = it.dateOfBirth
            weight = it.weight.toString()
            height = it.height.toString()
            gender = it.gender
            goal = it.goal
        }
    }

    var showSuccessSnackbar by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.updateProfilePicture(uri.toString())
            }
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .clickable {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                if (profile?.profilePictureUri != null) {
                    AsyncImage(
                        model = profile?.profilePictureUri,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        Icons.Default.Person, 
                        null, 
                        tint = MaterialTheme.colorScheme.primary, 
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Perfil Biométrico", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineSmall)
            Text("Mantém os teus dados físicos atualizados.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Dados Físicos", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    var showDatePicker by remember { mutableStateOf(false) }
                    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateOfBirth)

                    val formattedDate = remember(dateOfBirth) {
                        if (dateOfBirth != null) {
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateOfBirth!!))
                        } else {
                            ""
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true }
                    ) {
                        OutlinedTextField(
                            value = formattedDate,
                            onValueChange = { },
                            label = { Text("Data de Nascimento") },
                            enabled = false,
                            readOnly = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    if (showDatePicker) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    dateOfBirth = datePickerState.selectedDateMillis
                                    showDatePicker = false
                                }) {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) {
                                    Text("Cancelar")
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = height,
                            onValueChange = { height = it },
                            label = { Text("Altura (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Objetivo de Fitness", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    
                    val goals = listOf(
                        "Cut" to "Perder Peso (Défice)",
                        "Maintain" to "Manter Forma",
                        "Bulk" to "Ganhar Massa (Superávit)"
                    )
                    
                    goals.forEach { (goalId, goalLabel) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = goal == goalId,
                                onClick = { goal = goalId }
                            )
                            Text(goalLabel, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    val weightVal = weight.toDoubleOrNull() ?: 0.0
                    val heightVal = height.toDoubleOrNull() ?: 0.0
                    if (name.isNotEmpty() && dateOfBirth != null && weightVal > 0.0 && heightVal > 0.0) {
                        viewModel.createProfile(name, dateOfBirth!!, weightVal, heightVal, gender, goal)
                        showSuccessSnackbar = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Save, null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar Alterações")
            }
        }

        item {
            OutlinedButton(
                onClick = { viewModel.signOut() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text("Encerrar Sessão")
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }

    if (showSuccessSnackbar) {
        Snackbar(
            action = {
                TextButton(onClick = { showSuccessSnackbar = false }) {
                    Text("OK", color = MaterialTheme.colorScheme.inversePrimary)
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Perfil e metas atualizados com sucesso!")
        }
    }
}