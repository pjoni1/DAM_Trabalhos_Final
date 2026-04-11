package dam.exer_4

import java.util.Random
import kotlin.math.sqrt

data class Vec2(var x:Double,var y:Double) {
    operator fun plus(otherVector: Vec2): Vec2 { //usa se o this para diferenciar do outro vetor
        return Vec2(this.x + otherVector.x,this.y + otherVector.y)
    }

    operator fun minus(otherVector: Vec2): Vec2 {
        return Vec2(this.x - otherVector.x,this.y - otherVector.y)
    }

    operator fun times(multiplier: Double): Vec2 {
        return Vec2(x * multiplier,y * multiplier)
    }

    operator fun unaryMinus(): Vec2 {
        return Vec2(-x,-y)
    }

    fun magnitude(): Double {
        return sqrt(x*x + y*y)
    }

    fun dot(otherVector: Vec2): Double {
        return this.x * otherVector.x + this.y * otherVector.y
    }

    fun normalized(): Vec2 {
        val comp = this.magnitude()
        if (comp == 0.0){
            throw IllegalStateException()
        }else{
            return Vec2(x/comp,y/comp)
        }
    }

    operator fun compareTo(otherVector: Vec2): Int {
        val mag = this.magnitude()
        val otherMag = otherVector.magnitude()

        return mag.compareTo(otherMag)
    }

    operator fun get(index: Int): Double {
        if(index == 0){
            return x
        }else if(index == 1){
            return y
        }else{
            throw IndexOutOfBoundsException()
        }
    }



}


fun main () {
    val a = Vec2 (3.0 , 4.0)
    val b = Vec2 (1.0 , 2.0)
    println ("a = $a ") // a = Vec2 (x =3.0 , y =4.0)
    println ("b = $b ") // b = Vec2 (x =1.0 , y =2.0)
    println ("a + b = ${a + b}") // a + b = Vec2 (x =4.0 , y =6.0)
    println ("a - b = ${a - b}") // a - b = Vec2 (x =2.0 , y =2.0)
    println ("a * 2.0 = ${a * 2.0} ") // a * 2.0 = Vec2 (x =6.0 , y =8.0)
    println (" -a = ${-a}") // -a = Vec2 (x = -3.0 , y = -4.0)

    println ("|a| = ${a.magnitude()}") // |a| = 5.0
    println ("a dot b = ${a.dot(b)}") // a dot b = 11.0
    println (" norm (a) = ${a.normalized() }") // norm (a) = Vec2 (x =0.6 , y =0.8)

    println ("a [0] = ${a [0]} ") // a [0] = 3.0
    println ("a [1] = ${a [1]} ") // a [1] = 4.0

    println ("a > b = ${a > b}") // a > b = true
    println ("a < b = ${a < b}") // a < b = false

    val vectors = listOf ( Vec2 (1.0 , 0.0) , Vec2 (3.0 , 4.0) , Vec2 (0.0 , 2.0) )
    //println (" Longest = ${vectors.max()}") // Longest = Vec2 (x =3.0 , y =4.0)
    //println (" Shortest = ${vectors.min()}") // Shortest = Vec2 (x =1.0 , y =0.0)
}