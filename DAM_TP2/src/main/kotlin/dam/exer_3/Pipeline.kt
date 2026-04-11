package dam.exer_3

class Pipeline {

    val stepsList = mutableListOf<Pair<String, (List<String>) -> List<String>>>()

    fun addStage(name: String, transform: (List<String>) -> List<String>) {
        stepsList.add(Pair(name,transform))
    }

    fun execute(input: List<String>) : List<String> {
        var result = input
        for ((name,transform) in stepsList) {
            result = transform(result)
        }
        return result
    }

    fun describe(){
        for ((name,transform) in stepsList) {
            println(name)
        }
    }


}

fun buildPipeline(lambda: Pipeline.() -> Unit) : Pipeline { //aceita um lambda com pipeline
    val pipeline = Pipeline()   //cria instacia de pipeline
    pipeline.lambda()           //aplica o lambda ao pipeline
    return pipeline             //retorna
}


fun main(){
    val logs = listOf (
        " INFO : server started " ,
        " ERROR : disk full " ,
        " DEBUG : checking config " ,
        " ERROR : out of memory " ,
        " INFO : request received " ,
        " ERROR : connection timeout "
    )

    val pipeline = buildPipeline {
        addStage("Trim"){}

        addStage("Filter errors"){}

        addStage("Uppercase"){}

        addStage("Add index"){}
    }

    pipeline.describe()

    val results = pipeline.execute(logs)
    for (result in results){
        println(result)
    }
}