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

}