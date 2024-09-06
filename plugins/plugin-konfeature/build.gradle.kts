plugins {
    id("convention.plugin")
    alias(stack.plugins.kotlin.compose)
}

description = "Plugin for konfeature library integration"

android {
    namespace = "com.redmadrobot.debug.plugin.konfeature"
}

dependencies {
    implementation(androidx.lifecycle.runtime)
}
