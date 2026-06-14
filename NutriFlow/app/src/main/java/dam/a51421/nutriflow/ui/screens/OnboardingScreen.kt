package dam.a51421.nutriflow.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam.a51421.nutriflow.ui.viewmodel.NutriFlowViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import dam.a51421.nutriflow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: NutriFlowViewModel,
    onComplete: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    val context = LocalContext.current
    
    // Form States
    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf<Long?>(null) }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var goal by remember { mutableStateOf("Maintain") } // "Cut", "Bulk", "Maintain"

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("NutriFlow", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    Icon(
                        Icons.Default.Eco,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(4) { index ->
                    val isCompleted = index + 1 <= step
                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 8.dp)
                            .padding(horizontal = 4.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCompleted) MaterialTheme.colorScheme.primary
                                else Color.LightGray.copy(alpha = 0.5f)
                            )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Animated slide content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = step,
                    transitionSpec = {
                        fadeIn().togetherWith(fadeOut())
                    },
                    label = "step_transition"
                ) { currentStep ->
                    when (currentStep) {
                        1 -> StepWelcomeAndName(
                            name = name,
                            onNameChange = { name = it }
                        )
                        2 -> StepGenderAndAge(
                            gender = gender,
                            onGenderChange = { gender = it },
                            dateOfBirth = dateOfBirth,
                            onDateChange = { dateOfBirth = it }
                        )
                        3 -> StepBiometrics(
                            weight = weight,
                            onWeightChange = { weight = it },
                            height = height,
                            onHeightChange = { height = it }
                        )
                        4 -> StepGoal(
                            selectedGoal = goal,
                            onGoalSelected = { goal = it }
                        )
                    }
                }
            }

            // Error display
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Bottom Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = {
                            step--
                            errorMessage = null
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.back))
                    }
                    Spacer(Modifier.width(16.dp))
                }

                Button(
                    onClick = {
                        // Validation
                        val errName = context.getString(R.string.enter_name_error)
                        val errDob = context.getString(R.string.enter_dob_error)
                        val errWeight = context.getString(R.string.enter_weight_error)
                        val errHeight = context.getString(R.string.enter_height_error)
                        when (step) {
                            1 -> {
                                if (name.trim().isEmpty()) {
                                    errorMessage = errName
                                } else {
                                    errorMessage = null
                                    step++
                                }
                            }
                            2 -> {
                                if (dateOfBirth == null) {
                                    errorMessage = errDob
                                } else {
                                    errorMessage = null
                                    step++
                                }
                            }
                            3 -> {
                                val weightVal = weight.toDoubleOrNull()
                                val heightVal = height.toDoubleOrNull()
                                if (weightVal == null || weightVal <= 0.0) {
                                    errorMessage = errWeight
                                } else if (heightVal == null || heightVal <= 0.0) {
                                    errorMessage = errHeight
                                } else {
                                    errorMessage = null
                                    step++
                                }
                            }
                            4 -> {
                                val dobVal = dateOfBirth!!
                                val weightVal = weight.toDouble()
                                val heightVal = height.toDouble()
                                
                                viewModel.createProfile(
                                    name = name,
                                    dateOfBirth = dobVal,
                                    weight = weightVal,
                                    height = heightVal,
                                    gender = gender,
                                    goal = goal
                                )
                                onComplete()
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(if (step == 4) R.string.start else R.string.next))
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        if (step == 4) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next"
                    )
                }
            }
        }
    }
}

@Composable
fun StepWelcomeAndName(
    name: String,
    onNameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Eco,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(24.dp))
        
        Text(
            text = stringResource(R.string.onboarding_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.onboarding_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.your_name)) },
            placeholder = { Text(stringResource(R.string.ex_name)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepGenderAndAge(
    gender: String,
    onGenderChange: (String) -> Unit,
    dateOfBirth: Long?,
    onDateChange: (Long?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.personal_data),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.onboarding_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(Modifier.height(24.dp))

        // Gender Selection Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                onClick = { onGenderChange("Male") },
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (gender == "Male") MaterialTheme.colorScheme.primaryContainer
                                     else MaterialTheme.colorScheme.surface
                ),
                border = if (gender == "Male") null else BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Male, 
                        contentDescription = "Homem",
                        tint = if (gender == "Male") MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.male), 
                        fontWeight = FontWeight.Bold,
                        color = if (gender == "Male") MaterialTheme.colorScheme.primary else Color.Black
                    )
                }
            }

            Card(
                onClick = { onGenderChange("Female") },
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (gender == "Female") MaterialTheme.colorScheme.primaryContainer
                                     else MaterialTheme.colorScheme.surface
                ),
                border = if (gender == "Female") null else BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Female, 
                        contentDescription = "Mulher",
                        tint = if (gender == "Female") MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.female), 
                        fontWeight = FontWeight.Bold,
                        color = if (gender == "Female") MaterialTheme.colorScheme.primary else Color.Black
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        var showDatePicker by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()

        val formattedDate = remember(dateOfBirth) {
            if (dateOfBirth != null) {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(dateOfBirth))
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
                label = { Text(stringResource(R.string.date_of_birth)) },
                placeholder = { Text(stringResource(R.string.select_date)) },
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
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) }
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        onDateChange(datePickerState.selectedDateMillis)
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun StepBiometrics(
    weight: String,
    onWeightChange: (String) -> Unit,
    height: String,
    onHeightChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.physical_data),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.keep_physical_data_updated),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = onWeightChange,
            label = { Text(stringResource(R.string.weight_kg)) },
            placeholder = { Text(stringResource(R.string.ex_weight)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.FitnessCenter, contentDescription = null) }
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = height,
            onValueChange = onHeightChange,
            label = { Text(stringResource(R.string.height_cm)) },
            placeholder = { Text(stringResource(R.string.ex_height)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            leadingIcon = { Icon(Icons.Default.Height, contentDescription = null) }
        )
    }
}

@Composable
fun StepGoal(
    selectedGoal: String,
    onGoalSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.goal),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.fitness_goal),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(Modifier.height(24.dp))

        val goals = listOf(
            Triple("Cut", stringResource(R.string.lose_weight), Icons.AutoMirrored.Filled.TrendingDown),
            Triple("Maintain", stringResource(R.string.maintain_weight), Icons.AutoMirrored.Filled.TrendingFlat),
            Triple("Bulk", stringResource(R.string.gain_muscle), Icons.AutoMirrored.Filled.TrendingUp)
        )

        goals.forEach { (goalId, goalText, icon) ->
            val isSelected = selectedGoal == goalId
            Card(
                onClick = { onGoalSelected(goalId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(68.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                     else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon, 
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        goalText,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black
                    )
                    Spacer(Modifier.weight(1f))
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
