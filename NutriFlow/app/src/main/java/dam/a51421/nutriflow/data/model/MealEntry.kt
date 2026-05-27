package dam.a51421.nutriflow.data.model

data class MealEntry(
    val id: String,
    val foodName: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val quantity: Double, // in grams or portions
    val timestamp: Long = System.currentTimeMillis(),
    val type: String // "Free Entry" or "Plan Item"
)
