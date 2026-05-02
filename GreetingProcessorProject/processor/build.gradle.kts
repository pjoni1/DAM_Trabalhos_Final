plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
    implementation("com.squareup:kotlinpoet:1.14.2")
    // Agora isto vai funcionar porque o Gradle vai procurar na Raiz
    implementation(project(":annotations"))
}

kotlin {
    jvmToolchain(23)
}

kapt {
    correctErrorTypes = true
}