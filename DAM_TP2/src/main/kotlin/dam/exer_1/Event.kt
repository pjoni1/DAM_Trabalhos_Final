package dam.exer_1

import java.sql.Timestamp

sealed interface Event{
    val username: String
    val timestamp: Long
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


fun processEvents(events: List<Event>, handler: (Event) -> Unit){
    for(event in events){
        handler(event)
    }
}

