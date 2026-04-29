plugins {
    // Apenas define as versões para os outros usarem
    kotlin("jvm") version "2.1.10" apply false
    kotlin("kapt") version "2.1.10" apply false
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"
}