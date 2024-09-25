plugins {
    id("convention.debug.panel.plugin")
    alias(stack.plugins.kotlin.compose)
}

description = "Plugin for flipper library integration"

android {
    namespace = "com.redmadrobot.debug.plugin.flipper"
}

dependencies {
    implementation(androidx.lifecycle.runtime)
}
