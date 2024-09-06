plugins {
    id("convention.plugin")
    alias(stack.plugins.kotlin.compose)
}

description = "Plugin for flipper library integration"

android {
    namespace = "com.redmadrobot.debug.plugin.flipper"
}

dependencies {
    implementation(project(":panel-core"))
    implementation(project(":panel-common"))
    implementation(androidx.lifecycle.runtime)
}
