import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1-rc2"
    kotlin("plugin.serialization") version "1.6.10"
}

group = "ru.gerasimov"
version = "1.0"

repositories {
    jcenter()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation(project(":KRobot"))
    implementation(project(":KRobot:KAS"))
    implementation(project(":KRobot:tcpClient"))

    implementation(project(":RXTX"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("com.github.EvanRupert:ExcelKt:v0.1.2")

    implementation("br.com.devsrsouza.compose.icons.jetbrains:css-gg:1.0.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KRobotUtils"
            packageVersion = "1.0.0"
        }
    }
}