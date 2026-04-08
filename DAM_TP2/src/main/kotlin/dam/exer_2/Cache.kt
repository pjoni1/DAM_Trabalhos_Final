package dam.exer_2

class Cache <K : Any, V : Any>{
    val map = mutableMapOf<K, V>()

    fun put(key: K, value: V){
        map.put(key, value)
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


    fun getOrPut(key: K, default: () -> V): V {
        return map.getOrPut(key,default)

    }
    fun transform(key: K , action: (V) -> V): Boolean {
        val value = map[key] //testa se existe

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