plugins {
    kotlin("jvm")
    java
}

group = "ru.gerasimov"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // https://mvnrepository.com/artifact/com.neuronrobotics/nrjavaserial
    implementation("com.neuronrobotics:nrjavaserial:5.1.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}