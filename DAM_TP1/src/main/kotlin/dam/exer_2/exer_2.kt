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

    try{
        val resposta = scanner.next()
        when (resposta){
            "1" ->{
                println("Introduza o primeiro valor:")
                val valor1 = scanner.nextFloat()
                println("Introduza o segundo valor:")
                val valor2 = scanner.nextFloat()

                val resultado = valor1 + valor2
                println("Resultado da soma: $resultado")

                val bits = resultado.toBits()
                val hex = Integer.toHexString(bits)
                println("Resultado hexadecimal: $hex")

            }
            "2" ->{
                println("Introduza o primeiro valor:")
                val valor1 = scanner.nextFloat()
                println("Introduza o segundo valor:")
                val valor2 = scanner.nextFloat()

                val resultado = valor1 - valor2
                println("Resultado da subtração: $resultado")

                val bits = resultado.toBits()
                val hex = Integer.toHexString(bits)
                println("Resultado hexadecimal: $hex")

            }
            "3" ->{
                println("Introduza o primeiro valor:")
                val valor1 = scanner.nextFloat()
                println("Introduza o segundo valor:")
                val valor2 = scanner.nextFloat()

                val resultado = valor1 * valor2
                println("Resultado da multiplicação: $resultado")

                val bits = resultado.toBits()
                val hex = Integer.toHexString(bits)
                println("Resultado hexadecimal: $hex")
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
                    println("Resultado da divisão: $resultado")

                    val bits = resultado.toBits()
                    val hex = Integer.toHexString(bits)
                    println("Resultado hexadecimal: $hex")
                }

            }
            "5" ->{
                println("Introduza o primeiro valor booleano:")

                val valor1 = scanner.nextBoolean()
                println("Introduza o segundo valor booleano:")
                val valor2 = scanner.nextBoolean()

                val resultado = valor1 && valor2
                println("Resultado da operação booleana - AND: $resultado ")

            }
            "6" ->{
                println("Introduza o primeiro valor booleano:")

                val valor1 = scanner.nextBoolean()
                println("Introduza o segundo valor booleano:")
                val valor2 = scanner.nextBoolean()

                val resultado = valor1 || valor2
                println("Resultado da operação booleana - OR: $resultado ")

            }
            "7" ->{
                println("Introduza um valor booleano:")

                val valor1 = scanner.nextBoolean()


                val resultado = !valor1
                println("Resultado da operação booleana - NOT: $resultado")

            }

            "8" ->{
                println("Introduza um valor inteiro:")
                val valor1 = scanner.nextInt()

                println("Introduza o numero de posições para shiftar")
                val valor2 = scanner.nextInt()

                val resultado = valor1 shl valor2
                println("Resultado do shift esquerdo: $resultado")


            }

            "9" ->{
                println("Introduza um valor inteiro:")
                val valor1 = scanner.nextInt()

                println("Introduza o numero de posições para shiftar")
                val valor2 = scanner.nextInt()

                val resultado = valor1 shr valor2
                println("Resultado do shift direito: $resultado")


            }
            else -> println("Opção invalida")
        }
    }catch(e : InputMismatchException){
        println("Erro: O valor introduzido não é válido para esta operação!")
    }catch (e: Exception) {
        println("Ocorreu um erro inesperado.")
    }


}