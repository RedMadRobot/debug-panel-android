plugins {
    `kotlin-dsl`
}

group = "com.redmadrobot.build"

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("PublishPlugin") {
            id = "publishPlugin"
            implementationClass = "com.redmadrobot.build.PublishPlugin"
        }
    }
}

dependencies {
    implementation(kotlin("gradle-plugin", version = "1.6.20"))
    implementation("com.android.tools.build:gradle:8.3.2")
}
