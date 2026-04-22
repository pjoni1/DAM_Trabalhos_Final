package com.example.app
import com.example.generated.MyClassWrapper

fun main () {
    val myClass = MyClass()
    val wrappedMyClass = MyClassWrapper(myClass) // Use the wrapper class
    wrappedMyClass.sayHello()
    wrappedMyClass.compute()
}