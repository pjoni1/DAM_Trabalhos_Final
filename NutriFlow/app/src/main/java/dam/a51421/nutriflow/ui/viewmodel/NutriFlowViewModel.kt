package dam.a51421.nutriflow.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import android.content.Intent
import android.net.Uri
import dam.a51421.nutriflow.data.model.MealEntry
import dam.a51421.nutriflow.data.model.MealPlan
import dam.a51421.nutriflow.data.model.MediaEntry
import dam.a51421.nutriflow.data.model.TargetFood
import dam.a51421.nutriflow.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class NutriFlowViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("NutriFlowPrefs", Context.MODE_PRIVATE)

    // Perfil do Utilizador
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    // Registos de Refeição de Hoje
    private val _mealEntries = MutableStateFlow<List<MealEntry>>(emptyList())
    val mealEntries: StateFlow<List<MealEntry>> = _mealEntries.asStateFlow()

    // Plano Alimentar Ativo
    private val _currentMealPlan = MutableStateFlow<MealPlan?>(null)
    val currentMealPlan: StateFlow<MealPlan?> = _currentMealPlan.asStateFlow()

    // Registos de Fotos/Media no Vault
    private val _mediaEntries = MutableStateFlow<List<MediaEntry>>(emptyList())
    val mediaEntries: StateFlow<List<MediaEntry>> = _mediaEntries.asStateFlow()

    init {
        loadProfileFromPreferences()
        loadDefaultMealPlan()
        loadMediaFromPreferences()
    }

    // Carregar Perfil Guardado
    private fun loadProfileFromPreferences() {
        val name = sharedPreferences.getString("name", null)
        if (name != null) {
            val age = sharedPreferences.getInt("age", 25)
            val weight = sharedPreferences.getFloat("weight", 70f).toDouble()
            val height = sharedPreferences.getFloat("height", 170f).toDouble()
            val gender = sharedPreferences.getString("gender", "Male") ?: "Male"
            val goal = sharedPreferences.getString("goal", "Maintain") ?: "Maintain"
            val calorieGoal = sharedPreferences.getInt("calorieGoal", 2000)
            val proteinGoal = sharedPreferences.getInt("proteinGoal", 130)
            val carbsGoal = sharedPreferences.getInt("carbsGoal", 220)
            val fatsGoal = sharedPreferences.getInt("fatsGoal", 65)
            val streak = sharedPreferences.getInt("streak", 0)

            _userProfile.value = UserProfile(
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
                streakCount = streak
            )
        }
    }

    // Criar e Calcular Perfil (Onboarding)
    fun createProfile(name: String, age: Int, weight: Double, height: Double, gender: String, goal: String) {
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

        val profile = UserProfile(
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
            streakCount = 0
        )

        // Guardar localmente
        sharedPreferences.edit().apply {
            putString("name", name)
            putInt("age", age)
            putFloat("weight", weight.toFloat())
            putFloat("height", height.toFloat())
            putString("gender", gender)
            putString("goal", goal)
            putInt("calorieGoal", calorieGoal)
            putInt("proteinGoal", proteinGoal)
            putInt("carbsGoal", carbsGoal)
            putInt("fatsGoal", fatsGoal)
            putInt("streak", 0)
            apply()
        }

        _userProfile.value = profile
    }

    // Apagar Perfil (Reset)
    fun clearProfile() {
        sharedPreferences.edit().clear().apply()
        _userProfile.value = null
        _mealEntries.value = emptyList()
    }

    // Carregar Plano Alimentar Default de Exemplo
    private fun loadDefaultMealPlan() {
        val defaultFoods = listOf(
            TargetFood("Overnight Oats com Chia", 320, 12, 45, 8, "08:00 AM"),
            TargetFood("Café Negro", 5, 0, 1, 0, "08:00 AM"),
            TargetFood("Salada de Frango Grelhado", 450, 40, 15, 22, "12:30 PM"),
            TargetFood("Porção de Quinoa", 110, 4, 20, 2, "12:30 PM"),
            TargetFood("Salmão com Brócolos cozidos", 520, 42, 10, 28, "08:00 PM")
        )
        _currentMealPlan.value = MealPlan(
            id = "default_plan",
            dayOfWeek = 1, // Exemplo: Segunda-feira
            targetFoods = defaultFoods
        )
    }

    // Adicionar Refeição ao Registo Livre
    fun logManualFood(name: String, calories: Int, protein: Int, carbs: Int, fats: Int, quantity: Double) {
        val newEntry = MealEntry(
            id = UUID.randomUUID().toString(),
            foodName = name,
            calories = calories,
            protein = protein,
            carbs = carbs,
            fats = fats,
            quantity = quantity,
            type = "Free Entry"
        )
        _mealEntries.value = _mealEntries.value + newEntry
        checkAndIncrementStreak()
    }

    // Registar/Marcar Alimento do Plano (Checklist)
    fun logPlanFood(targetFood: TargetFood) {
        // Verificar se já está registado hoje para evitar duplicados
        val alreadyLogged = _mealEntries.value.any { it.foodName == targetFood.name && it.type == "Plan Item" }
        if (!alreadyLogged) {
            val newEntry = MealEntry(
                id = UUID.randomUUID().toString(),
                foodName = targetFood.name,
                calories = targetFood.calories,
                protein = targetFood.protein,
                carbs = targetFood.carbs,
                fats = targetFood.fats,
                quantity = 1.0,
                type = "Plan Item"
            )
            _mealEntries.value = _mealEntries.value + newEntry
            checkAndIncrementStreak()
        }
    }

    // Desmarcar/Remover Alimento do Plano
    fun removePlanFood(targetFood: TargetFood) {
        _mealEntries.value = _mealEntries.value.filterNot { it.foodName == targetFood.name && it.type == "Plan Item" }
    }

    // Remover qualquer registo
    fun removeMealEntry(entryId: String) {
        _mealEntries.value = _mealEntries.value.filterNot { it.id == entryId }
    }

    // Lógica simples de Streaks (se atingir 85%+ da meta calórica ou completar itens principais)
    private fun checkAndIncrementStreak() {
        val profile = _userProfile.value ?: return
        val totalCaloriesConsumed = _mealEntries.value.sumOf { it.calories }
        
        // Se estiver dentro de 85% e 115% da meta
        val minTarget = profile.dailyCalorieGoal * 0.85
        val maxTarget = profile.dailyCalorieGoal * 1.15
        
        if (totalCaloriesConsumed.toDouble() in minTarget..maxTarget) {
            val lastActive = sharedPreferences.getLong("lastActive", 0)
            val today = System.currentTimeMillis() / (24 * 60 * 60 * 1000)
            val lastDay = lastActive / (24 * 60 * 60 * 1000)
            
            if (today > lastDay) {
                val newStreak = profile.streakCount + 1
                sharedPreferences.edit().putInt("streak", newStreak).putLong("lastActive", System.currentTimeMillis()).apply()
                _userProfile.value = profile.copy(streakCount = newStreak)
            }
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
    }

    // Remover foto do Vault
    fun removeMediaEntry(id: String) {
        val newList = _mediaEntries.value.filterNot { it.id == id }
        _mediaEntries.value = newList
        saveMediaToPreferences(newList)
    }
}
