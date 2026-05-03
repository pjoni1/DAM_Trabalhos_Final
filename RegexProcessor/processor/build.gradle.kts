plugins {
    kotlin("jvm")
    kotlin("kapt")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // 1. Dependência do teu módulo de anotações
    implementation(project(":annotations"))

    // 2. Google Auto Service (para registar o processador)
    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")

    // 3. KotlinPoet (para gerar o código .kt)
    implementation("com.squareup:kotlinpoet:1.14.2")
}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}