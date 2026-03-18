package dam.exer_1

val events = listOf (
    Event.Login (" alice " , 1_000 ) ,
    Event.Purchase (" alice " , 49.99 , 1_100 ) ,
    Event.Purchase (" bob " , 19.99 , 1_200 ) ,
    Event.Login (" bob " , 1_050 ) ,
    Event.Purchase (" alice " , 15.00 , 1_300 ) ,
    Event.Logout (" alice " , 1_400 ) ,
    Event.Logout (" bob " , 1_500 )
)