package dam.exer_1

class Logout(val username: String, val timestamp: Long): EventIO() {
    override fun toString(): String {
        return "[LOGOUT] $username logged out at t=$timestamp"
    }
}