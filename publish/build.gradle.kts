plugins {
    `kotlin-dsl`
}

group = "com.redmadrobot.debug"

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("PublishPlugin") {
            id = "publishPlugin"
            implementationClass = "com.redmadrobot.debug.publish.PublishPlugin"
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.0")
}
