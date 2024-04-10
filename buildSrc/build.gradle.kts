plugins {
    `kotlin-dsl`
}

group = "com.redmadrobot.build"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(kotlin("gradle-plugin", version = "1.9.23"))
    implementation("com.android.tools.build:gradle:8.3.2")
}
