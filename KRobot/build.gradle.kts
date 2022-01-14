plugins {
    kotlin("jvm")
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
}