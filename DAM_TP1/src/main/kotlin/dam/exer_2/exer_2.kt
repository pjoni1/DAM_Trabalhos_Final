package dam.exer_2

import java.util.*

fun main(){
    val scanner = Scanner(System.`in`)

    println("Escolha uma opção:")
    println("1 - Adição")
    println("2 - Subtração")
    println("3 - Multiplicação")
    println("4 - Divisão")

    println("5 - Operação booleana - AND")
    println("6 - Operação booleana - OR")
    println("7 - Operação booleana - NOT")

    println("8 - Shift Left")
    println("9 - Shift Right")

    val resposta = scanner.next()
    when (resposta){
        "1" ->{
            println("Introduza o primeiro valor:")
            val valor1 = scanner.nextFloat()
            println("Introduza o segundo valor:")
            val valor2 = scanner.nextFloat()

            val resultado = valor1 + valor2
            println("Resultado da soma: ")
            println(resultado)

            println("Resultado hexadecimal: ")
            val bits = resultado.toBits()
            val hex = Integer.toHexString(bits)
            println(hex)
        }
        "2" ->{
            println("Introduza o primeiro valor:")
            val valor1 = scanner.nextFloat()
            println("Introduza o segundo valor:")
            val valor2 = scanner.nextFloat()

            val resultado = valor1 - valor2
            println("Resultado da subtração: ")
            println(resultado)

            println("Resultado hexadecimal: ")
            val bits = resultado.toBits()
            val hex = Integer.toHexString(bits)
            println(hex)
        }
        "3" ->{
            println("Introduza o primeiro valor:")
            val valor1 = scanner.nextFloat()
            println("Introduza o segundo valor:")
            val valor2 = scanner.nextFloat()

            val resultado = valor1 * valor2
            println("Resultado da multiplicação: ")
            println(resultado)

            println("Resultado hexadecimal: ")
            val bits = resultado.toBits()
            val hex = Integer.toHexString(bits)
            println(hex)
        }
        "4" ->{
            println("Introduza o primeiro valor:")
            val valor1 = scanner.nextFloat()
            println("Introduza o segundo valor:")
            val valor2 = scanner.nextFloat()

            if(valor2 == 0f) {
                println("O segundo valor não pode ser 0")
            }else{
                val resultado = valor1 / valor2
                println("Resultado da divisão: ")
                println(resultado)

                println("Resultado hexadecimal: ")
                val bits = resultado.toBits()
                val hex = Integer.toHexString(bits)
                println(hex)
            }

        }
        "5" ->{
            println("Introduza o primeiro valor booleano:")

            val valor1 = scanner.nextBoolean()
            println("Introduza o segundo valor booleano:")
            val valor2 = scanner.nextBoolean()

            if((valor1 or valor2) != (true or false)) {
                println("Introduza valores booleanos apenas!")
            }else{
                val resultado = valor1 && valor2
                println("Resultado da operação booleana - AND ")
                println(resultado)
            }

        }
        "6" ->{
            println("Introduza o primeiro valor booleano:")

            val valor1 = scanner.nextBoolean()
            println("Introduza o segundo valor booleano:")
            val valor2 = scanner.nextBoolean()

            if((valor1 or valor2) != (true or false)) {
                println("Introduza valores booleanos apenas!")
            }else{
                val resultado = valor1 || valor2
                println("Resultado da operação booleana - OR ")
                println(resultado)
            }

        }
        "7" ->{
            println("Introduza um valor booleano:")

            val valor1 = scanner.nextBoolean()


            if((valor1) != (true or false)) {
                println("Introduza um valor booleano apenas!")
            }else{
                val resultado = !valor1
                println("Resultado da operação booleana - NOT ")
                println(resultado)
            }

        }

        "8" ->{
            println("Introduza um valor inteiro:")
            val valor1 = scanner.nextInt()

            println("Introduza o numero de posições para shiftar")
            val valor2 = scanner.nextInt()

            val resultado = valor1 shl valor2
            println("Resultado do shift esquerdo")
            println(resultado)


        }

        "9" ->{
            println("Introduza um valor inteiro:")
            val valor1 = scanner.nextInt()

            println("Introduza o numero de posições para shiftar")
            val valor2 = scanner.nextInt()

            val resultado = valor1 shr valor2
            println("Resultado do shift direito")
            println(resultado)


        }
        else -> println("Opção invalida")
    }
}