package dam.a51421.nutriflow.data.model

data class TargetFood(
    val name: String,
    val calories: Int,
    val protein: Int, // in grams
    val carbs: Int,   // in grams
    val fats: Int,    // in grams
    val time: String = "08:00 AM" // e.g. breakfast/lunch time
)

data class MealPlan(
    val id: String,
    val dayOfWeek: Int, // 1 to 7 (Monday to Sunday)
    val targetFoods: List<TargetFood>
)
