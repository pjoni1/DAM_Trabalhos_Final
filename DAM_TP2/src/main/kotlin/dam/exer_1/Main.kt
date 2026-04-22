package dam.exer_1

val events = listOf (
    Login ("alice" , 1_000 ) ,
    Purchase ("alice" , 49.99 , 1_100 ) ,
    Purchase ("bob" , 19.99 , 1_200 ) ,
    Login ("bob" , 1_050 ) ,
    Purchase ("alice" , 15.00 , 1_300 ) ,
    Logout ("alice" , 1_400 ) ,
    Logout ("bob" , 1_500 )
)

fun main(){
    processEvents(events) { event ->
        when (event) {
            is Login -> println("${event.username} logged in at $event.timestamp")
            is Purchase -> println("$event.username spent at $event.timestamp")
            is Logout -> println("$event.username logged out at $event.timestamp")
        }
    }

    println("Total spent by alice: ${events.totalSpent("alice")}" )
    println("Total spent by bob: ${events.totalSpent("bob")}" )

    println("Events for alice:")
    val aliceEvents = events.filterByUser("alice")
    for (event in aliceEvents){
        println(event)
    }
}
