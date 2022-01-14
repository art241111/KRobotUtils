plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    java
}

group = "ru.geraimovAD"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project("KAS"))
    implementation(project("tcpClient"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}