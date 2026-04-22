package dam.exer_2

class Cache <K : Any, V : Any>{
    val map = mutableMapOf<K, V>()

    fun put(key: K, value: V){
        map[key] = value
    }

    fun get(key: K): V? {
        return map.get(key)
    }

    fun evict(key: K){
        map.remove(key)
    }

    fun size(): Int{
        return map.size //tmb podia ser usado map.count()
    }

    //tenta encontrar key no mapa, se encontrar devolve, senão executa a função lambda
    fun getOrPut(key: K, default: () -> V): V {
        return map.getOrPut(key,default) //default e não {default} porque default já é uma função
    }

    fun transform(key: K , action: (V) -> V): Boolean {
        val value = map[key] //pega o valor

        if(value != null){ //se existir aplica a função lambda
            val newValue = action(value)
            map[key] = newValue //atualiza com o novo valor
            return true
        }else{
            return false
        }
    }

    fun snapshot(): Map<K, V>{
        val copy = map.toMap() // faz uma copia imutavel(não alteravel, não se pode aplicar metodos put ou remove)
        return copy
    }

    //fun filterValues(predicate: (V) -> Boolean): Map<K, V>{

    //}
}


fun main() {
    val wordFreqCache = Cache<String, Int>()
    wordFreqCache.put("kotlin",1)
    wordFreqCache.put("scala",1)
    wordFreqCache.put("haskell",1)

    println("--- Word frequency cache ---")
    println("Size: ${wordFreqCache.size()}" )
    println("Frequency of \"kotlin\": ${wordFreqCache.get("kotlin")}")
    println("getOrPut \"kotlin\": ${wordFreqCache.getOrPut("kotlin"){0}}") //se não houver kotlin retorna 0
    println("getOrPut \"kotlin\": ${wordFreqCache.getOrPut("java"){0}}") //se não houver java retorna 0
    println("Size after getOrPut : ${wordFreqCache.size()}") //adicionou o java porque não havia
    println("Transform \"kotlin\" (+1) : ${wordFreqCache.transform("kotlin"){ it + 1 }}") //it é o parametro do lamba, ou seja, o valor atual que está
    println("Transform\" cobol\" (+1) : ${wordFreqCache.transform("cobol"){ it + 1 }}")   // guardado no cache para aquela chave

    println("Snapshot: ${wordFreqCache.snapshot()}")


    val idRegistryCache = Cache<Int, String>()
    idRegistryCache.put(1,"Alice")
    idRegistryCache.put(2,"Bob")
    println("--- Id registry cache ---")
    println("Id 1 -> ${idRegistryCache.get(1)}")
    println("Id 2 -> ${idRegistryCache.get(2)}")

    idRegistryCache.evict(1)
    println("After evict id 1, size : ${idRegistryCache.size()}")
    println("Id 1 after evict -> ${idRegistryCache.get(1)}")

}