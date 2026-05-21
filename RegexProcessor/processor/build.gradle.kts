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
    implementation(project(":annotations"))

    compileOnly("com.google.auto.service:auto-service-annotations:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")

    implementation("com.squareup:kotlinpoet:1.14.2")
}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}