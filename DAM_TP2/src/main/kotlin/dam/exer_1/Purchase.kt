package dam.exer_1

class Purchase(override val username: String, val amount: Double, override val timestamp: Long): EventIO() {

}