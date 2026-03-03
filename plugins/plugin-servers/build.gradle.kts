plugins {
    alias(stack.plugins.ksp)
    id("convention.debug.panel.plugin")
}

description = "Plugin for switching server hosts"

android {
    namespace = "com.redmadrobot.debug.plugin.servers"
}

dependencies {
    implementation(androidx.core)
    implementation(androidx.room)
    implementation(androidx.room.runtime)
    implementation(stack.kotlinx.serialization.json)
    implementation(stack.okhttp)
    ksp(androidx.room.compiler)
}
