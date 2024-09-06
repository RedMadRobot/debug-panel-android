plugins {
    id("convention.plugin")
    alias(stack.plugins.ksp)
    alias(stack.plugins.kotlin.compose)
}

description = "Plugin for switching server hosts"

android {
    namespace = "com.redmadrobot.debug.plugin.servers"
}

dependencies {
    implementation(stack.kotlinx.serialization.json)
    ksp(androidx.room.compiler)
}
