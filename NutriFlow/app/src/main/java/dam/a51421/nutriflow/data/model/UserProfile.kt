package dam.a51421.nutriflow.data.model

data class UserProfile(
    val name: String,
    val age: Int,
    val weight: Double, // in kg
    val height: Double, // in cm
    val gender: String, // "Male" or "Female"
    val goal: String,   // "Cut", "Bulk", or "Maintain"
    val dailyCalorieGoal: Int,
    val dailyProteinGoal: Int, // in grams
    val dailyCarbsGoal: Int,   // in grams
    val dailyFatsGoal: Int,    // in grams
    val streakCount: Int = 0,
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)
