package dam.exer_1

sealed interface Event

fun List<Event>.filterByUser(username: String) : List<Event>{
    return this.filterByUser(username)
}

fun List<Event>.totalSpent(username : String): List<Event>{
    return this.filterByUser(username)
}