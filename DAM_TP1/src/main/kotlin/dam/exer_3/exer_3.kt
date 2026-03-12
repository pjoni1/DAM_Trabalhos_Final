package dam.exer_3

fun main() {
    val bounces = generateSequence(100.0) { it * 0.6 }
        .takeWhile { it >= 1.0 }
        .take(15)
        .toList()

    println(bounces)

}