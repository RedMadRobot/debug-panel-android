plugins {
    id("convention.debug.panel.plugin")
    alias(stack.plugins.kotlin.serialization)
}

description = "Plugin for switching server hosts"

android {
    namespace = "com.redmadrobot.debug.plugin.servers"
}

dependencies {
    implementation(androidx.core)
    implementation(androidx.datastore)
    implementation(stack.kotlinx.serialization.json)
    implementation(stack.okhttp)
}
