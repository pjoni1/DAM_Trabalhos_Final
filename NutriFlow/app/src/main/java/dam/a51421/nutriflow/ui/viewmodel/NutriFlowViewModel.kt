package dam.a51421.nutriflow.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import android.content.Intent
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import dam.a51421.nutriflow.data.model.MealEntry
import dam.a51421.nutriflow.data.model.MealPlan
import dam.a51421.nutriflow.data.model.MediaEntry
import dam.a51421.nutriflow.data.model.TargetFood
import dam.a51421.nutriflow.data.model.UserProfile
import dam.a51421.nutriflow.data.model.DatabaseFood
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import androidx.lifecycle.viewModelScope
import java.util.UUID
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import android.graphics.Bitmap
import org.json.JSONObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NutriFlowViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("NutriFlowPrefs", Context.MODE_PRIVATE)
    private val db = FirebaseFirestore.getInstance()

    // Auth State
    private val _isUserAuthenticated = MutableStateFlow(sharedPreferences.getString("auth_username", null) != null)
    val isUserAuthenticated: StateFlow<Boolean> = _isUserAuthenticated.asStateFlow()

    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    // Perfil do Utilizador
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    // Registos de Refeição de Hoje
    private val _mealEntries = MutableStateFlow<List<MealEntry>>(emptyList())
    val mealEntries: StateFlow<List<MealEntry>> = _mealEntries.asStateFlow()

    // Plano Alimentar Ativo
    private val _currentMealPlan = MutableStateFlow<MealPlan?>(null)
    val currentMealPlan: StateFlow<MealPlan?> = _currentMealPlan.asStateFlow()

    private val _selectedDateOffset = MutableStateFlow(0)
    val selectedDateOffset: StateFlow<Int> = _selectedDateOffset.asStateFlow()

    private fun getStartOfDayMillis(offset: Int): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_YEAR, offset)
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    val filteredMealEntries: StateFlow<List<MealEntry>> = combine(
        _mealEntries, _selectedDateOffset
    ) { entries, offset ->
        val startOfDay = getStartOfDayMillis(offset)
        val endOfDay = startOfDay + 86400000L
        entries.filter { it.timestamp in startOfDay until endOfDay }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun changeDateOffset(delta: Int) {
        val newOffset = (_selectedDateOffset.value + delta).coerceIn(-3, 3)
        _selectedDateOffset.value = newOffset
        loadDefaultMealPlan()
    }

    // Registos de Fotos/Media no Vault
    private val _mediaEntries = MutableStateFlow<List<MediaEntry>>(emptyList())
    val mediaEntries: StateFlow<List<MediaEntry>> = _mediaEntries.asStateFlow()

    // Linguagem
    private val _currentLanguage = MutableStateFlow("en")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun changeLanguage(languageCode: String) {
        sharedPreferences.edit().putString("language", languageCode).apply()
        _currentLanguage.value = languageCode
    }

    init {
        val savedLang = sharedPreferences.getString("language", "pt") ?: "pt"
        _currentLanguage.value = savedLang

        loadProfileFromPreferences()
        loadDefaultMealPlan()
        loadMediaFromPreferences()
        loadMealEntriesFromPreferences()
        recalculateStreak()
        
        // Se estiver logado, garante que trazemos tudo da Cloud
        val uid = sharedPreferences.getString("id", null)
        if (_isUserAuthenticated.value && uid != null) {
            loadProfileFromFirestore(uid)
        }
    }

    fun calculateAge(dobMillis: Long): Int {
        val dob = java.util.Calendar.getInstance().apply { timeInMillis = dobMillis }
        val today = java.util.Calendar.getInstance()
        var calculatedAge = today.get(java.util.Calendar.YEAR) - dob.get(java.util.Calendar.YEAR)
        if (today.get(java.util.Calendar.DAY_OF_YEAR) < dob.get(java.util.Calendar.DAY_OF_YEAR)) {
            calculatedAge--
        }
        return calculatedAge
    }

    // Carregar Perfil Guardado
    private fun loadProfileFromPreferences() {
        val name = sharedPreferences.getString("name", null)
        if (name != null) {
            var id = sharedPreferences.getString("id", null)
            if (id == null) {
                id = UUID.randomUUID().toString()
                sharedPreferences.edit().putString("id", id).apply()
            }
            val age = sharedPreferences.getInt("age", 25)
            val dob = sharedPreferences.getLong("dateOfBirth", -1L)
            val dateOfBirth = if (dob != -1L) dob else null
            val profilePictureUri = sharedPreferences.getString("profilePictureUri", null)
            val weight = sharedPreferences.getFloat("weight", 70f).toDouble()
            val height = sharedPreferences.getFloat("height", 170f).toDouble()
            val gender = sharedPreferences.getString("gender", "Male") ?: "Male"
            val goal = sharedPreferences.getString("goal", "Maintain") ?: "Maintain"
            val calorieGoal = sharedPreferences.getInt("calorieGoal", 2000)
            val proteinGoal = sharedPreferences.getInt("proteinGoal", 130)
            val carbsGoal = sharedPreferences.getInt("carbsGoal", 220)
            val fatsGoal = sharedPreferences.getInt("fatsGoal", 65)
            val streak = sharedPreferences.getInt("streak", 0)
            val lastActive = sharedPreferences.getLong("lastActive", System.currentTimeMillis())

            _userProfile.value = UserProfile(
                id = id,
                name = name,
                age = age,
                weight = weight,
                height = height,
                gender = gender,
                goal = goal,
                dailyCalorieGoal = calorieGoal,
                dailyProteinGoal = proteinGoal,
                dailyCarbsGoal = carbsGoal,
                dailyFatsGoal = fatsGoal,
                streakCount = streak,
                lastActiveTimestamp = lastActive,
                dateOfBirth = dateOfBirth,
                profilePictureUri = profilePictureUri
            )
        }
    }

    // Criar e Calcular Perfil (Onboarding)
    fun createProfile(name: String, dateOfBirth: Long, weight: Double, height: Double, gender: String, goal: String) {
        val age = calculateAge(dateOfBirth)
        // Obter ou gerar um ID único para o perfil
        val id = sharedPreferences.getString("id", null) ?: UUID.randomUUID().toString()

        // Mifflin-St Jeor Formula para TMB
        val bmr = if (gender == "Male") {
            (10 * weight) + (6.25 * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25 * height) - (5 * age) - 161
        }

        // Ajustar caloria total com base no objetivo
        val activityFactor = 1.2 // Fator sedentário/leve por defeito
        val maintenanceCalories = bmr * activityFactor

        val calorieGoal = when (goal) {
            "Cut" -> (maintenanceCalories - 500).toInt().coerceAtLeast(1200)
            "Bulk" -> (maintenanceCalories + 300).toInt()
            else -> maintenanceCalories.toInt()
        }

        // Distribuição de Macros base
        val proteinPerKg = if (goal == "Cut" || goal == "Bulk") 2.0 else 1.6
        val proteinGoal = (weight * proteinPerKg).toInt()
        
        // Gorduras: 25% das calorias totais
        val fatsGoal = ((calorieGoal * 0.25) / 9).toInt()
        
        // Hidratos de Carbono: Restante
        val remainingCalories = calorieGoal - (proteinGoal * 4) - (fatsGoal * 9)
        val carbsGoal = (remainingCalories / 4).toInt().coerceAtLeast(50)

        val existingProfile = _userProfile.value
        val streakToKeep = existingProfile?.streakCount ?: 0
        val lastActiveToKeep = existingProfile?.lastActiveTimestamp ?: 0L
        val picToKeep = existingProfile?.profilePictureUri

        val profile = UserProfile(
            id = id,
            name = name,
            age = age,
            weight = weight,
            height = height,
            gender = gender,
            goal = goal,
            dailyCalorieGoal = calorieGoal,
            dailyProteinGoal = proteinGoal,
            dailyCarbsGoal = carbsGoal,
            dailyFatsGoal = fatsGoal,
            streakCount = streakToKeep,
            lastActiveTimestamp = lastActiveToKeep,
            dateOfBirth = dateOfBirth,
            profilePictureUri = picToKeep
        )

        // Guardar localmente
        sharedPreferences.edit().apply {
            putString("id", id)
            putString("name", name)
            putInt("age", age)
            putLong("dateOfBirth", dateOfBirth)
            putFloat("weight", weight.toFloat())
            putFloat("height", height.toFloat())
            putString("gender", gender)
            putString("goal", goal)
            putInt("calorieGoal", calorieGoal)
            putInt("proteinGoal", proteinGoal)
            putInt("carbsGoal", carbsGoal)
            putInt("fatsGoal", fatsGoal)
            putInt("streak", streakToKeep)
            putLong("lastActive", lastActiveToKeep)
            if (picToKeep != null) putString("profilePictureUri", picToKeep)
            apply()
        }

        _userProfile.value = profile
        
        // Sincronizar com Firestore
        syncAllToFirestore()
    }

    fun updateProfilePicture(uri: String) {
        val profile = _userProfile.value ?: return
        val updatedProfile = profile.copy(profilePictureUri = uri)
        _userProfile.value = updatedProfile
        
        sharedPreferences.edit().putString("profilePictureUri", uri).apply()
        
        // Save to Firestore if authenticated
        val uid = sharedPreferences.getString("id", null)
        if (_isUserAuthenticated.value && uid != null) {
            db.collection("profiles").document(uid).update("profilePictureUri", uri)
        }
    }

    // Apagar Perfil (Reset) / Encerrar Sessão
    fun signOut() {
        _isUserAuthenticated.value = false
        clearProfile()
    }

    fun clearProfile() {
        sharedPreferences.edit().clear().apply()
        _userProfile.value = null
        _mealEntries.value = emptyList()
        _mediaEntries.value = emptyList()
    }

    // Carregar Plano Alimentar Default de Exemplo
    private fun loadDefaultMealPlan() {
        val offset = _selectedDateOffset.value
        val startOfDayMillis = getStartOfDayMillis(offset)
        
        // Use an absolute day index so the meal plan is always the same for a specific date
        val dayIndex = (startOfDayMillis / 86400000L).toInt()
        val planType = Math.abs(dayIndex) % 3

        val defaultFoods = when (planType) {
            1 -> listOf(
                TargetFood("Ovos Mexidos com Pão Integral", 350, 20, 30, 15, "08:00 AM"),
                TargetFood("Chá Verde", 0, 0, 0, 0, "08:00 AM"),
                TargetFood("Bife de Peru com Arroz", 480, 45, 40, 10, "12:30 PM"),
                TargetFood("Maçã", 95, 0, 25, 0, "12:30 PM"),
                TargetFood("Sopa de Legumes e Pescada", 320, 30, 20, 5, "08:00 PM")
            )
            2 -> listOf(
                TargetFood("Batido de Proteína e Banana", 280, 25, 35, 5, "08:00 AM"),
                TargetFood("Massa Integral com Atum", 550, 35, 60, 15, "12:30 PM"),
                TargetFood("Salada Mista", 50, 2, 10, 0, "12:30 PM"),
                TargetFood("Omelete de Claras com Espinafres", 250, 30, 5, 10, "08:00 PM")
            )
            else -> listOf( // Original (Type 0)
                TargetFood("Overnight Oats com Chia", 320, 12, 45, 8, "08:00 AM"),
                TargetFood("Café Negro", 5, 0, 1, 0, "08:00 AM"),
                TargetFood("Salada de Frango Grelhado", 450, 40, 15, 22, "12:30 PM"),
                TargetFood("Porção de Quinoa", 110, 4, 20, 2, "12:30 PM"),
                TargetFood("Salmão com Brócolos cozidos", 520, 42, 10, 28, "08:00 PM")
            )
        }

        val dayOfWeek = java.util.Calendar.getInstance().apply { timeInMillis = startOfDayMillis }.get(java.util.Calendar.DAY_OF_WEEK)

        _currentMealPlan.value = MealPlan(
            id = "plan_date_$dayIndex",
            dayOfWeek = dayOfWeek,
            targetFoods = defaultFoods
        )
    }

    // Adicionar Refeição ao Registo Livre
    fun logManualFood(name: String, calories: Int, protein: Int, carbs: Int, fats: Int, quantity: Double) {
        val offset = _selectedDateOffset.value
        val entryTimestamp = if (offset == 0) System.currentTimeMillis() else getStartOfDayMillis(offset) + 43200000L

        val newEntry = MealEntry(
            id = UUID.randomUUID().toString(),
            foodName = name,
            calories = calories,
            protein = protein,
            carbs = carbs,
            fats = fats,
            quantity = quantity,
            timestamp = entryTimestamp,
            type = "Free Entry"
        )
        _mealEntries.value = _mealEntries.value + newEntry
        saveMealEntriesToPreferences(_mealEntries.value)
        recalculateStreak()

        // Sincronizar com o Firestore
        val profile = _userProfile.value
        if (profile != null) {
            val entryData = hashMapOf(
                "foodName" to newEntry.foodName,
                "calories" to newEntry.calories,
                "protein" to newEntry.protein,
                "carbs" to newEntry.carbs,
                "fats" to newEntry.fats,
                "quantity" to newEntry.quantity,
                "timestamp" to newEntry.timestamp,
                "type" to newEntry.type
            )
            db.collection("profiles").document(profile.id).collection("mealEntries").document(newEntry.id)
                .set(entryData)
        }
    }

    // Registar/Marcar Alimento do Plano (Checklist)
    fun logPlanFood(targetFood: TargetFood) {
        val startOfDay = getStartOfDayMillis(_selectedDateOffset.value)
        val endOfDay = startOfDay + 86400000L
        val alreadyLogged = _mealEntries.value.any { 
            it.foodName == targetFood.name && 
            it.type == "Plan Item" && 
            it.timestamp in startOfDay until endOfDay
        }
        
        if (!alreadyLogged) {
            val offset = _selectedDateOffset.value
            val entryTimestamp = if (offset == 0) System.currentTimeMillis() else getStartOfDayMillis(offset) + 43200000L

            val newEntry = MealEntry(
                id = UUID.randomUUID().toString(),
                foodName = targetFood.name,
                calories = targetFood.calories,
                protein = targetFood.protein,
                carbs = targetFood.carbs,
                fats = targetFood.fats,
                quantity = 1.0,
                timestamp = entryTimestamp,
                type = "Plan Item"
            )
            _mealEntries.value = _mealEntries.value + newEntry
            saveMealEntriesToPreferences(_mealEntries.value)
            recalculateStreak()

            // Sincronizar com o Firestore
            val profile = _userProfile.value
            if (profile != null) {
                val entryData = hashMapOf(
                    "foodName" to newEntry.foodName,
                    "calories" to newEntry.calories,
                    "protein" to newEntry.protein,
                    "carbs" to newEntry.carbs,
                    "fats" to newEntry.fats,
                    "quantity" to newEntry.quantity,
                    "timestamp" to newEntry.timestamp,
                    "type" to newEntry.type
                )
                db.collection("profiles").document(profile.id).collection("mealEntries").document(newEntry.id)
                    .set(entryData)
            }
        }
    }

    // Desmarcar/Remover Alimento do Plano
    fun removePlanFood(targetFood: TargetFood) {
        val startOfDay = getStartOfDayMillis(_selectedDateOffset.value)
        val endOfDay = startOfDay + 86400000L

        val entryToRemove = _mealEntries.value.find { 
            it.foodName == targetFood.name && 
            it.type == "Plan Item" &&
            it.timestamp in startOfDay until endOfDay
        }

        if (entryToRemove != null) {
            _mealEntries.value = _mealEntries.value.filter { it.id != entryToRemove.id }
            saveMealEntriesToPreferences(_mealEntries.value)
            recalculateStreak()
            
            val profile = _userProfile.value
            if (profile != null) {
                db.collection("profiles").document(profile.id).collection("mealEntries").document(entryToRemove.id).delete()
            }
        }
    }

    // Remover qualquer registo
    fun removeMealEntry(entryId: String) {
        val profile = _userProfile.value
        if (profile != null) {
            db.collection("profiles").document(profile.id).collection("mealEntries").document(entryId).delete()
        }
        _mealEntries.value = _mealEntries.value.filterNot { it.id == entryId }
        saveMealEntriesToPreferences(_mealEntries.value)
        recalculateStreak()
    }

    // Lógica simples de Streaks (se atingir 85%+ da meta calórica)
    // Atualiza baseada no dia de hoje e de ontem
    private fun recalculateStreak() {
        val profile = _userProfile.value ?: return
        
        // Removemos o maxTarget. Se a pessoa comer a mais, continua a ter a streak porque cumpriu o mínimo.
        val minTarget = profile.dailyCalorieGoal * 0.85
        
        val startToday = getStartOfDayMillis(0)
        val endToday = startToday + 86400000L
        val calsToday = _mealEntries.value.filter { it.timestamp in startToday until endToday }.sumOf { it.calories }
        val completedToday = calsToday >= minTarget
        
        val startYesterday = getStartOfDayMillis(-1)
        val endYesterday = startYesterday + 86400000L
        val calsYesterday = _mealEntries.value.filter { it.timestamp in startYesterday until endYesterday }.sumOf { it.calories }
        val completedYesterday = calsYesterday >= minTarget

        val lastStreakDate = sharedPreferences.getLong("lastStreakDate", 0L)
        var currentStreak = profile.streakCount
        
        if (completedToday) {
            // Se hoje foi concluído
            if (lastStreakDate < startToday) {
                // Streak não foi dada hoje
                if (lastStreakDate == startYesterday || completedYesterday) {
                    currentStreak += 1
                } else {
                    currentStreak = 1
                }
                
                sharedPreferences.edit().putLong("lastStreakDate", startToday).apply()
                db.collection("profiles").document(profile.id).update(mapOf("lastStreakDate" to startToday))
            }
        } else {
            // Se hoje não foi concluído, verificamos se a streak tinha sido dada hoje e revertemos
            if (lastStreakDate == startToday) {
                currentStreak = (currentStreak - 1).coerceAtLeast(0)
                sharedPreferences.edit().putLong("lastStreakDate", startYesterday).apply()
                db.collection("profiles").document(profile.id).update(mapOf("lastStreakDate" to startYesterday))
            }
            
            // Se ontem também não cumpriu e o lastStreakDate já é velho, vai a 0
            if (lastStreakDate < startYesterday && !completedYesterday) {
                currentStreak = 0
            }
        }
        
        // Se houver diferença, atualiza
        if (profile.streakCount != currentStreak) {
            _userProfile.value = profile.copy(streakCount = currentStreak)
            sharedPreferences.edit().putInt("streak", currentStreak).apply()
            db.collection("profiles").document(profile.id).update(mapOf("streakCount" to currentStreak))
        }
    }

    // Carregar registos de media guardados
    private fun loadMediaFromPreferences() {
        val serialized = sharedPreferences.getString("mediaEntries", null) ?: return
        val list = serialized.split("|ENTRY|").mapNotNull { entryStr ->
            val parts = entryStr.split("|_|")
            if (parts.size >= 4) {
                MediaEntry(
                    id = parts[0],
                    filePath = parts[1],
                    category = parts[2],
                    date = parts[3].toLongOrNull() ?: System.currentTimeMillis()
                )
            } else {
                null
            }
        }
        _mediaEntries.value = list
    }

    // Guardar registos de media localmente
    private fun saveMediaToPreferences(entries: List<MediaEntry>) {
        val serialized = entries.joinToString("|ENTRY|") { "${it.id}|_|${it.filePath}|_|${it.category}|_|${it.date}" }
        sharedPreferences.edit().putString("mediaEntries", serialized).apply()
    }

    // Carregar refeições guardadas localmente
    private fun loadMealEntriesFromPreferences() {
        val serialized = sharedPreferences.getString("localMealEntries", null) ?: return
        val list = serialized.split("|ENTRY|").mapNotNull { entryStr ->
            val parts = entryStr.split("|_|")
            if (parts.size >= 9) {
                try {
                    MealEntry(
                        id = parts[0],
                        foodName = parts[1],
                        calories = parts[2].toInt(),
                        protein = parts[3].toInt(),
                        carbs = parts[4].toInt(),
                        fats = parts[5].toInt(),
                        quantity = parts[6].toDouble(),
                        timestamp = parts[7].toLong(),
                        type = parts[8]
                    )
                } catch (e: Exception) { null }
            } else null
        }
        _mealEntries.value = list
    }

    // Guardar refeições localmente
    private fun saveMealEntriesToPreferences(entries: List<MealEntry>) {
        val serialized = entries.joinToString("|ENTRY|") { 
            "${it.id}|_|${it.foodName}|_|${it.calories}|_|${it.protein}|_|${it.carbs}|_|${it.fats}|_|${it.quantity}|_|${it.timestamp}|_|${it.type}"
        }
        sharedPreferences.edit().putString("localMealEntries", serialized).apply()
    }

    // Adicionar nova foto ao Vault
    fun addMediaEntry(uri: Uri, category: String) {
        try {
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
            getApplication<Application>().contentResolver.takePersistableUriPermission(uri, takeFlags)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val newEntry = MediaEntry(
            id = UUID.randomUUID().toString(),
            filePath = uri.toString(),
            category = category,
            date = System.currentTimeMillis()
        )
        val newList = _mediaEntries.value + newEntry
        _mediaEntries.value = newList
        saveMediaToPreferences(newList)

        // Sincronizar com o Firestore
        val profile = _userProfile.value
        if (profile != null) {
            val mediaData = hashMapOf(
                "filePath" to newEntry.filePath,
                "storageUrl" to newEntry.storageUrl,
                "category" to newEntry.category,
                "date" to newEntry.date
            )
            db.collection("profiles").document(profile.id).collection("vault").document(newEntry.id)
                .set(mediaData)
        }
    }

    // Remover foto do Vault
    fun removeMediaEntry(id: String) {
        val newList = _mediaEntries.value.filterNot { it.id == id }
        _mediaEntries.value = newList
        saveMediaToPreferences(newList)

        // Sincronizar com o Firestore
        val profile = _userProfile.value
        if (profile != null) {
            db.collection("profiles").document(profile.id).collection("vault").document(id)
                .delete()
        }
    }

    // Sincronização completa de tudo com o Firestore
    fun syncAllToFirestore() {
        val profile = _userProfile.value ?: return
        val profileId = profile.id

        // 1. Sincronizar perfil
        val profileData = hashMapOf<String, Any>(
            "name" to profile.name,
            "age" to profile.age,
            "weight" to profile.weight,
            "height" to profile.height,
            "gender" to profile.gender,
            "goal" to profile.goal,
            "dailyCalorieGoal" to profile.dailyCalorieGoal,
            "dailyProteinGoal" to profile.dailyProteinGoal,
            "dailyCarbsGoal" to profile.dailyCarbsGoal,
            "dailyFatsGoal" to profile.dailyFatsGoal,
            "streakCount" to profile.streakCount,
            "lastActiveTimestamp" to profile.lastActiveTimestamp
        )
        if (profile.dateOfBirth != null) profileData["dateOfBirth"] = profile.dateOfBirth
        if (profile.profilePictureUri != null) profileData["profilePictureUri"] = profile.profilePictureUri
        db.collection("profiles").document(profileId).set(profileData)

        // 2. Sincronizar vault (fotos)
        _mediaEntries.value.forEach { media ->
            val mediaData = hashMapOf(
                "filePath" to media.filePath,
                "storageUrl" to media.storageUrl,
                "category" to media.category,
                "date" to media.date
            )
            db.collection("profiles").document(profileId).collection("vault").document(media.id).set(mediaData)
        }

        // 3. Sincronizar mealEntries (logs de comida)
        _mealEntries.value.forEach { entry ->
            val entryData = hashMapOf(
                "foodName" to entry.foodName,
                "calories" to entry.calories,
                "protein" to entry.protein,
                "carbs" to entry.carbs,
                "fats" to entry.fats,
                "quantity" to entry.quantity,
                "timestamp" to entry.timestamp,
                "type" to entry.type
            )
            db.collection("profiles").document(profileId).collection("mealEntries").document(entry.id).set(entryData)
        }

        // 4. Sincronizar plano alimentar
        val plan = _currentMealPlan.value
        if (plan != null) {
            val planData = hashMapOf(
                "dayOfWeek" to plan.dayOfWeek
            )
            db.collection("profiles").document(profileId).collection("mealPlan").document("activePlan").set(planData)

            plan.targetFoods.forEachIndexed { index, food ->
                val foodData = hashMapOf(
                    "name" to food.name,
                    "calories" to food.calories,
                    "protein" to food.protein,
                    "carbs" to food.carbs,
                    "fats" to food.fats,
                    "time" to food.time
                )
                db.collection("profiles").document(profileId).collection("mealPlan").document("activePlan")
                    .collection("targetFoods").document("food_$index").set(foodData)
            }
        }
    }

    // --- Autenticação (Apenas Firestore) ---
    fun signIn(username: String, pass: String) {
        _isAuthLoading.value = true
        _authError.value = null
        
        val userDoc = username.trim().lowercase()
        db.collection("users").document(userDoc).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val doc = task.result
                if (doc != null && doc.exists()) {
                    val savedPass = doc.getString("password")
                    if (savedPass == pass) {
                        val profileId = doc.getString("profileId") ?: UUID.randomUUID().toString()
                        sharedPreferences.edit().putString("auth_username", userDoc).putString("id", profileId).apply()
                        loadProfileFromFirestore(profileId, onComplete = {
                            _isUserAuthenticated.value = true
                            _isAuthLoading.value = false
                        })
                    } else {
                        _isAuthLoading.value = false
                        _authError.value = "Palavra-passe incorreta."
                    }
                } else {
                    _authError.value = "Utilizador não encontrado."
                }
            } else {
                _authError.value = "Erro ao conectar à base de dados."
            }
        }
    }

    fun signUp(username: String, pass: String) {
        _isAuthLoading.value = true
        _authError.value = null
        
        val userDoc = username.trim().lowercase()
        db.collection("users").document(userDoc).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val doc = task.result
                if (doc != null && doc.exists()) {
                    _isAuthLoading.value = false
                    _authError.value = "Este nome de utilizador já existe."
                } else {
                    // Criar novo user
                    val profileId = UUID.randomUUID().toString()
                    val userData = hashMapOf(
                        "username" to username.trim(),
                        "password" to pass,
                        "profileId" to profileId
                    )
                    db.collection("users").document(userDoc).set(userData).addOnCompleteListener { createTask ->
                        _isAuthLoading.value = false
                        if (createTask.isSuccessful) {
                            sharedPreferences.edit().putString("auth_username", userDoc).putString("id", profileId).apply()
                            _isUserAuthenticated.value = true
                        } else {
                            _authError.value = "Erro ao registar utilizador."
                        }
                    }
                }
            } else {
                _isAuthLoading.value = false
                _authError.value = "Erro ao conectar à base de dados."
            }
        }
    }

    fun clearAuthError() {
        _authError.value = null
    }

    private fun loadProfileFromFirestore(uid: String, onComplete: (() -> Unit)? = null) {
        db.collection("profiles").document(uid).get().addOnSuccessListener { doc ->
            if (doc.exists()) {
                val name = doc.getString("name") ?: ""
                val age = doc.getLong("age")?.toInt() ?: 25
                val weight = doc.getDouble("weight") ?: 70.0
                val height = doc.getDouble("height") ?: 170.0
                val gender = doc.getString("gender") ?: "Male"
                val goal = doc.getString("goal") ?: "Maintain"
                val calorieGoal = doc.getLong("dailyCalorieGoal")?.toInt() ?: 2000
                val proteinGoal = doc.getLong("dailyProteinGoal")?.toInt() ?: 130
                val carbsGoal = doc.getLong("dailyCarbsGoal")?.toInt() ?: 220
                val fatsGoal = doc.getLong("dailyFatsGoal")?.toInt() ?: 65
                val streakCount = doc.getLong("streakCount")?.toInt() ?: 0
                val dateOfBirth = doc.getLong("dateOfBirth")
                val profilePictureUri = doc.getString("profilePictureUri")
                val lastActiveTimestamp = doc.getLong("lastActiveTimestamp") ?: System.currentTimeMillis()
                val lastStreakDate = doc.getLong("lastStreakDate") ?: 0L
                
                _userProfile.value = UserProfile(
                    id = uid,
                    name = name,
                    age = age,
                    weight = weight,
                    height = height,
                    gender = gender,
                    goal = goal,
                    dailyCalorieGoal = calorieGoal,
                    dailyProteinGoal = proteinGoal,
                    dailyCarbsGoal = carbsGoal,
                    dailyFatsGoal = fatsGoal,
                    streakCount = streakCount,
                    lastActiveTimestamp = lastActiveTimestamp,
                    dateOfBirth = dateOfBirth,
                    profilePictureUri = profilePictureUri
                )
                
                sharedPreferences.edit().apply {
                    putString("id", uid)
                    putString("name", name)
                    putInt("age", age)
                    if (dateOfBirth != null) putLong("dateOfBirth", dateOfBirth)
                    if (profilePictureUri != null) putString("profilePictureUri", profilePictureUri)
                    putFloat("weight", weight.toFloat())
                    putFloat("height", height.toFloat())
                    putString("gender", gender)
                    putString("goal", goal)
                    putInt("calorieGoal", calorieGoal)
                    putInt("proteinGoal", proteinGoal)
                    putInt("carbsGoal", carbsGoal)
                    putInt("fatsGoal", fatsGoal)
                    putInt("streak", streakCount)
                    putLong("lastActive", lastActiveTimestamp)
                    putLong("lastStreakDate", lastStreakDate)
                    apply()
                }

                // 1. Carregar fotos do Vault
                db.collection("profiles").document(uid).collection("vault").get()
                    .addOnSuccessListener { vaultSnapshot ->
                        val mediaList = vaultSnapshot.documents.mapNotNull { mediaDoc ->
                            val mId = mediaDoc.id
                            val filePath = mediaDoc.getString("filePath") ?: return@mapNotNull null
                            val category = mediaDoc.getString("category") ?: "Outro"
                            val date = mediaDoc.getLong("date") ?: System.currentTimeMillis()
                            MediaEntry(id = mId, filePath = filePath, category = category, date = date)
                        }
                        _mediaEntries.value = mediaList
                        saveMediaToPreferences(mediaList)
                    }

                // 2. Carregar Registos de Comida (Meal Entries)
                db.collection("profiles").document(uid).collection("mealEntries").get()
                    .addOnSuccessListener { entriesSnapshot ->
                        val entriesList = entriesSnapshot.documents.mapNotNull { entryDoc ->
                            val eId = entryDoc.id
                            val foodName = entryDoc.getString("foodName") ?: return@mapNotNull null
                            val calories = entryDoc.getLong("calories")?.toInt() ?: 0
                            val protein = entryDoc.getLong("protein")?.toInt() ?: 0
                            val carbs = entryDoc.getLong("carbs")?.toInt() ?: 0
                            val fats = entryDoc.getLong("fats")?.toInt() ?: 0
                            val quantity = entryDoc.getDouble("quantity") ?: 1.0
                            val timestamp = entryDoc.getLong("timestamp") ?: System.currentTimeMillis()
                            val type = entryDoc.getString("type") ?: "Free Entry"
                            MealEntry(
                                id = eId,
                                foodName = foodName,
                                calories = calories,
                                protein = protein,
                                carbs = carbs,
                                fats = fats,
                                quantity = quantity,
                                timestamp = timestamp,
                                type = type
                            )
                        }
                        _mealEntries.value = entriesList
                        saveMealEntriesToPreferences(entriesList)
                    }

                // 3. Carregar Plano Alimentar
                db.collection("profiles").document(uid).collection("mealPlan").document("activePlan").get()
                    .addOnSuccessListener { planDoc ->
                        if (planDoc.exists()) {
                            val dayOfWeek = planDoc.getLong("dayOfWeek")?.toInt() ?: 1
                            db.collection("profiles").document(uid).collection("mealPlan").document("activePlan")
                                .collection("targetFoods").get()
                                .addOnSuccessListener { foodsSnapshot ->
                                    val targetFoodsList = foodsSnapshot.documents.mapNotNull { foodDoc ->
                                        val fName = foodDoc.getString("name") ?: return@mapNotNull null
                                        val fCalories = foodDoc.getLong("calories")?.toInt() ?: 0
                                        val fProtein = foodDoc.getLong("protein")?.toInt() ?: 0
                                        val fCarbs = foodDoc.getLong("carbs")?.toInt() ?: 0
                                        val fFats = foodDoc.getLong("fats")?.toInt() ?: 0
                                        val fTime = foodDoc.getString("time") ?: ""
                                        TargetFood(fName, fCalories, fProtein, fCarbs, fFats, fTime)
                                    }
                                    _currentMealPlan.value = MealPlan(
                                        id = "activePlan",
                                        dayOfWeek = dayOfWeek,
                                        targetFoods = targetFoodsList
                                    )
                                    onComplete?.invoke()
                                }.addOnFailureListener { onComplete?.invoke() }
                        } else {
                            onComplete?.invoke()
                        }
                    }.addOnFailureListener { onComplete?.invoke() }
            } else {
                onComplete?.invoke()
            }
        }.addOnFailureListener { onComplete?.invoke() }
    }

    // --- PESQUISA DE ALIMENTOS (AUTOCOMPLETE) ---
    private val _foodSearchResults = MutableStateFlow<List<DatabaseFood>>(emptyList())
    val foodSearchResults: StateFlow<List<DatabaseFood>> = _foodSearchResults.asStateFlow()

    fun searchFoods(query: String) {
        if (query.isBlank()) {
            _foodSearchResults.value = emptyList()
            return
        }
        
        // Simples query com base no nome (começa com)
        val searchTerm = query.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
        
        db.collection("food_database")
            .whereGreaterThanOrEqualTo("name", searchTerm)
            .whereLessThanOrEqualTo("name", searchTerm + "\uf8ff")
            .limit(10)
            .get()
            .addOnSuccessListener { documents ->
                val results = documents.mapNotNull { doc ->
                    try {
                        DatabaseFood(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            calories = doc.getLong("calories")?.toInt() ?: 0,
                            protein = doc.getLong("protein")?.toInt() ?: 0,
                            carbs = doc.getLong("carbs")?.toInt() ?: 0,
                            fats = doc.getLong("fats")?.toInt() ?: 0,
                            defaultQuantity = doc.getDouble("defaultQuantity") ?: 100.0
                        )
                    } catch (e: Exception) { null }
                }
                _foodSearchResults.value = results
            }
            .addOnFailureListener {
                _foodSearchResults.value = emptyList()
            }
    }

    fun seedFoodDatabase() {
        val initialFoods = listOf(
            DatabaseFood(name = "Maçã", calories = 52, protein = 0, carbs = 14, fats = 0, defaultQuantity = 100.0),
            DatabaseFood(name = "Manga", calories = 60, protein = 1, carbs = 15, fats = 0, defaultQuantity = 100.0),
            DatabaseFood(name = "Melão", calories = 34, protein = 1, carbs = 8, fats = 0, defaultQuantity = 100.0),
            DatabaseFood(name = "Banana", calories = 89, protein = 1, carbs = 23, fats = 0, defaultQuantity = 100.0),
            DatabaseFood(name = "Peito de Frango", calories = 165, protein = 31, carbs = 0, fats = 3, defaultQuantity = 100.0),
            DatabaseFood(name = "Arroz Branco", calories = 130, protein = 2, carbs = 28, fats = 0, defaultQuantity = 100.0),
            DatabaseFood(name = "Arroz Integral", calories = 111, protein = 3, carbs = 23, fats = 1, defaultQuantity = 100.0),
            DatabaseFood(name = "Iogurte Grego", calories = 59, protein = 10, carbs = 3, fats = 0, defaultQuantity = 100.0),
            DatabaseFood(name = "Ovo Cozido", calories = 155, protein = 13, carbs = 1, fats = 11, defaultQuantity = 100.0),
            DatabaseFood(name = "Atum em Água", calories = 116, protein = 26, carbs = 0, fats = 1, defaultQuantity = 100.0),
            DatabaseFood(name = "Pão Integral", calories = 247, protein = 13, carbs = 41, fats = 3, defaultQuantity = 100.0)
        )

        db.collection("food_database").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                // Seed the database
                initialFoods.forEach { food ->
                    val foodData = hashMapOf(
                        "name" to food.name,
                        "calories" to food.calories,
                        "protein" to food.protein,
                        "carbs" to food.carbs,
                        "fats" to food.fats,
                        "defaultQuantity" to food.defaultQuantity
                    )
                    db.collection("food_database").add(foodData)
                }
            }
        }
    }

    private val _isAnalyzingFood = MutableStateFlow(false)
    val isAnalyzingFood: StateFlow<Boolean> = _isAnalyzingFood.asStateFlow()

    private val _analyzedFoodResult = MutableStateFlow<DatabaseFood?>(null)
    val analyzedFoodResult: StateFlow<DatabaseFood?> = _analyzedFoodResult.asStateFlow()

    private val _analysisError = MutableStateFlow<String?>(null)
    val analysisError: StateFlow<String?> = _analysisError.asStateFlow()

    fun resetAnalyzedFood() {
        _analyzedFoodResult.value = null
    }

    fun clearAnalysisError() {
        _analysisError.value = null
    }

    private fun scaleDownBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val maxDim = Math.max(bitmap.width, bitmap.height)
        if (maxDim <= maxDimension) return bitmap
        val scale = maxDimension.toFloat() / maxDim
        val newWidth = (bitmap.width * scale).toInt()
        val newHeight = (bitmap.height * scale).toInt()
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    fun analyzeFoodImage(bitmap: Bitmap) {
        _isAnalyzingFood.value = true
        _analysisError.value = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val scaledBitmap = scaleDownBitmap(bitmap, 800)
                // Initializing the Gemini model
                val generativeModel = GenerativeModel(
                    modelName = "gemini-flash-latest",
                    apiKey = "AIzaSyB_CXgeLU_743wF9TjLAH5MUrWbVETv5eo"
                )

                val prompt = "Analyze this image and identify the food. Estimate its macronutrients for a typical serving size in grams. Return ONLY a valid JSON object with the following keys exactly: foodName (String), calories (Int), qty (Double), protein (Int), carbs (Int), fats (Int). No markdown formatting, just the raw JSON."

                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )

                val responseText = response.text ?: ""
                val cleanJsonString = responseText.replace("```json", "").replace("```", "").trim()
                
                val jsonObject = JSONObject(cleanJsonString)
                val food = DatabaseFood(
                    name = jsonObject.optString("foodName", "Unknown Food"),
                    calories = jsonObject.optInt("calories", 0),
                    defaultQuantity = jsonObject.optDouble("qty", 100.0),
                    protein = jsonObject.optInt("protein", 0),
                    carbs = jsonObject.optInt("carbs", 0),
                    fats = jsonObject.optInt("fats", 0)
                )

                withContext(Dispatchers.Main) {
                    _analyzedFoodResult.value = food
                    _isAnalyzingFood.value = false
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    _analysisError.value = "Erro na análise: ${e.message}"
                    _isAnalyzingFood.value = false
                }
            }
        }
    }
}
