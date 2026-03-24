package dam.exer_1

sealed interface Event{
    val username: String
}

fun List<Event>.filterByUser(username: String) : List<Event>{
    val events: List<Event> = this
    val userEvents = mutableListOf<Event>()
    for (event in events){
        if(event.username == username){
            userEvents.add(event)
        }
    }
    return userEvents

}

fun List<Event>.totalSpent(username : String): Double{
    val events: List<Event> = this
    val userEvents = events.filterByUser(username)
    val purchases = userEvents.filterIsInstance<Purchase>()
    val totalSpent = purchases.sumOf { it.amount }
    return totalSpent
}

fun handler