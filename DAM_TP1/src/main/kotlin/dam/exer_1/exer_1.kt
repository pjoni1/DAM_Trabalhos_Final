package dam.exer_1

fun main() {

    val intArray = IntArray(50){i->(i + 1) * (i+ 1)}

    //range de 1 a 50
    val nums = (1..50).toList()

    val array = Array(50) { i -> (i + 1) * (i + 1) }

    println("a)")
    println(intArray.contentToString())

    println("b)")
    //map de cada valor da lista ao quadrado
    println(nums.map{it * it})

    println("c)")
    println(array.contentToString())
}
