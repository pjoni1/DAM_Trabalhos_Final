package dam.exer_1

import java.sql.Timestamp

class Login(val username: String, val timestamp: Long): EventIO() {
    override fun toString(): String {
        return "[Purchase] $username logged in at t=$timestamp"
    }
}