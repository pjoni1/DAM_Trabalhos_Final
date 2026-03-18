package dam.exer_1

class Purchase(val username: String, val amount: Double, val timestamp: Long): EventIO() {
    override fun toString(): String {
        return "[Purchase] $username spent $ $amount at t=$timestamp"
    }
}