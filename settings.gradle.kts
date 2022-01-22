pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
rootProject.name = "KRobotUtils"

include(":KRobot:tcpClient")
include(":KRobot:KAS")
include("KRobot")
include("RXTX")
