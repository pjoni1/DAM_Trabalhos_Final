package dam.exer_1

sealed interface Event

fun List<Event>.filterByUser(username: String) : List<Event>{
    var events: List<Event>
    var userEvents: List<String>
    for (event in events){
        if(event.username == username){
            userEvents.append(event)
        }
    }
    return userEvents

}

fun List<Event>.totalSpent(username : String): Double{
    var events: List<Event>
    var purchases = events.filterIsInstance<Purchase>()
    var totalSpent = purchases.sumOf { it.amount }
    return totalSpent

}

fun<List<Event>,